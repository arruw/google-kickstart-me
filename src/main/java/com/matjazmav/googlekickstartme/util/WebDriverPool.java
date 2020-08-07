package com.matjazmav.googlekickstartme.util;

import com.matjazmav.googlekickstartme.util.function.*;
import lombok.*;
import lombok.extern.log4j.*;
import org.apache.commons.pool2.impl.*;
import org.openqa.selenium.*;
import org.apache.commons.pool2.*;

import javax.annotation.*;
import java.util.function.*;

@Log4j2
public class WebDriverPool<T extends WebDriver> {

    private final GenericObjectPool<T> webDriverObjectPool;

    public WebDriverPool(Supplier<T> webDriverFactory, int poolSize) {
        this.webDriverObjectPool = new GenericObjectPool<T>(new BasePooledObjectFactory<T>() {
            @Override
            public T create() throws Exception {
                return webDriverFactory.get();
            }

            @Override
            public PooledObject<T> wrap(T obj) {
                return new DefaultPooledObject<>(obj);
            }

            @Override
            public void passivateObject(PooledObject<T> p) throws Exception {
                log.info("Passivating pooled object...");
                val webDriver = p.getObject();
                try {
                    val originalHandle = webDriver.getWindowHandle();
                    for(String handle : webDriver.getWindowHandles()) {
                        if (!handle.equals(originalHandle)) {
                            webDriver.switchTo().window(handle);
                            webDriver.close();
                        }
                    }
                    webDriver.switchTo().window(originalHandle);
                } finally {
                    webDriver.manage().deleteAllCookies();
                    super.passivateObject(p);
                }
            }

            @Override
            public void destroyObject(PooledObject<T> p) throws Exception {
                log.info("Destroying pooled object...");
                p.getObject().quit();
                super.destroyObject(p);
            }

            @Override
            public PooledObject<T> makeObject() throws Exception {
                log.info("Making pooled object...");
                return super.makeObject();
            }

            @Override
            public boolean validateObject(PooledObject<T> p) {
                log.info("Validating pooled object...");
                val webDriver = p.getObject();
                return webDriver.getWindowHandles().size() == 1 && webDriver.manage().getCookies().isEmpty();
            }

            @Override
            public void activateObject(PooledObject<T> p) throws Exception {
                log.info("Activating pooled object...");
                super.activateObject(p);
            }
        }, new GenericObjectPoolConfig<>(){{
            setMaxTotal(poolSize);
        }});
    }

    public <R> R execute(CheckedFunction<T,R> function) throws Exception {
        T obj = this.webDriverObjectPool.borrowObject();
        try{
            return function.apply(obj);
        } finally {
            this.webDriverObjectPool.returnObject(obj);
        }
    }

    @PreDestroy
    public void destroy() throws InterruptedException {
        log.info("Destroying object pool...");
        this.webDriverObjectPool.close();
        while(this.webDriverObjectPool.getNumActive() > 0) {
            log.info("Waiting object pool...");
            this.wait(1000);
        }
        this.webDriverObjectPool.clear();
    }
}