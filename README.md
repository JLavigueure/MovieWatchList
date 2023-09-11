<a name="readme-top"></a>

<br />
<div align="center">
  <a href="https://github.com/JLavigueure/MovieWatchList">
    <img src="images/clapboard.png" alt="Logo" width="80" height="80">
  </a>

  <h3 align="center">Movie Watchlist</h3>

  <p align="center">
    An awesome way to never forget about all those movies you found interesting.
    <br />
    <a href="https://github.com/JLavigueure/MovieWatchList"><strong>Explore the docs »</strong></a>
    <br />
    <br />
    <a href="https://github.com/JLavigueure/MovieWatchList">View Demo</a>
    ·
    <a href="https://github.com/JLavigueure/MovieWatchList/issues">Report Bug</a>
    ·
    <a href="https://github.com/JLavigueure/MovieWatchList/issues">Request Feature</a>
  </p>
</div>

## About The Project

<div align="center">
  <a href="https://github.com/JLavigueure/MovieWatchList">
    <img src="images/moviecollage.jpg" alt="Movie collage" width="500">
  </a>
</div>

Movie Watchlist is a terminal based program that allows you to query over 9 million titles via API, and save those movies you find interesting for a later time into a NoSQL (Json) database in your documents. In addition, it provides movie reccomendations based off your unique movie list by using details like your favorite genre and age of movie. Build your cinema agenda with Movie Watchlist.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

### Built With

* ![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
* ![Apache Maven](https://img.shields.io/badge/Apache%20Maven-C71A36?style=for-the-badge&logo=Apache%20Maven&logoColor=white)
* <a href="https://github.com/google/gson">Gson</a>
* <a href="https://github.com/junit-team">JUnit</a>

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- GETTING STARTED -->
## Getting Started

### Prerequisites
* <a href="https://maven.apache.org/download.cgi">Maven</a>

### Installation
1. Clone the repo in your designated folder
   ```sh
   git clone https://github.com/JLavigueure/MovieWatchList.git
   ```
2. Move into repo directory
   ```sh
   cd MovieWatchList/
   ```
 3. Install Maven packages to target folder
    ```sh
    mvn dependency:copy-dependencies 
    ```
4. Build into jar executable
   ```sh
   mvn package
   ```
5. Run 
   ```sh
   java -jar target/moviewatchlist-1.0.0.jar
   ```
<p align="right">(<a href="#readme-top">back to top</a>)</p>




