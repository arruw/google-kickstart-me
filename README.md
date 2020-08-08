# 🏆🏆🏆 Google Kick Start Badge 🏆🏆🏆

Show your problem solving skills with an embeddable Google Kick Start badge.
Embed your badge on your blog, code repository, forum, email signature...

[
    ![Google Kick Start Badge](https://google-kickstart-me.herokuapp.com/flier/matjazmav/java/thumbnail)
](https://google-kickstart-me.herokuapp.com/flier/matjazmav/java?link=https://www.linkedin.com/in/matjazmav/)

**Note:** This web app is currently deployed on a [Heroku](https://www.heroku.com) free plan.
Therefore, application goes to sleep after 30 minutes of inactivity, thus it is expected that there will be 
some lag when application wakes up and possible some timeouts.
**If there is interest, we can move it to paid plan or self host it.**

### Usage

**Markdown**
```md
[
    ![Google Kick Start Badge](https://google-kickstart-me.herokuapp.com/flier/<nickname>/<language>/thumbnail)
](https://google-kickstart-me.herokuapp.com/flier/<nickname>/<language>)
```

**HTML**
```html
<a href="https://google-kickstart-me.herokuapp.com/flier/<nickname>/<language>">
    <img
        alt="Google Kick Start Badge"
        src="https://google-kickstart-me.herokuapp.com/flier/<nickname>/<language>/thumbnail" />
</a>
```

```html
<iframe src="https://google-kickstart-me.herokuapp.com/flier/<nickname>/<language>" />
```

**API Endpoints**
```
# HTML
https://google-kickstart-me.herokuapp.com/flier/<nickname>/<language>[?link=<some-url>]

# IMAGE
https://google-kickstart-me.herokuapp.com/flier/<nickname>/<language>/thumbnail
```

|Option      |Required|Default|Description|
|------------|--------|-------|-----------|
|`<nickname>`| x      |       |           |
|`<language>`| x      |       |           |
|`<some-url>`|        |       |           |

Supported `<language>` values:
```
bash
c
cplusplus
csharp
clojure
go
groovy
java
javascript
php
python
ruby
rust
scala
swift
typescript
```

### Contributing
You are most welcome to submit a pull request of any kind (bug fixes, better UI design, 
optimization, additional tests, refactoring,
add support for additional programming language, new feature...)

#### TODO
- Add unit & integration tests
- Code cleanup

### Story behind the project
I started competing on [Kick Start](https://codingcompetitions.withgoogle.com/kickstart) in late 2019 because I wanted to practice problem solving
and to learn new programming languages. Lately I have been playing around in Java and [Spring](https://spring.io/) framework.
Actually, this is my first project with Spring Boot framework, and you are most welcome to do a code
review and suggest any improvements.

### Deployment notes
TODO

#### Configurations
```
SELENIUM_CHROME_PATH=<todo>
SELENIUM_CHROME_DRIVER_PATH=<todo>
```

### Credits
* [Google Kick Start](https://codingcompetitions.withgoogle.com/kickstart) - Competition homepage and data source for the badge
* [Heroku](https://www.heroku.com) - Free Hosting
* [Country Flags API](https://www.countryflags.io/) - Country flags images used on the badge
* [Devicon](https://devicons.github.io/devicon/) - Programming language images used on the badge
