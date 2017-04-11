<template>
  <div id="app container-fluid">
    <header class="header">
      <div class="row">
        <div class="small-8 large-10 columns text-left">
          <p class="reset-margin relative header--text">
            <strong class="header--text--title">Properties</strong>
            <span class="header--text--title__support">Editor</span>
          </p>
        </div>
        <div class="small-4 large-2 columns text-right header--nav">
          <router-link to="home" class="new--file">Upload new file</router-link>
        </div>
      </div>
    </header>
    <router-view></router-view>
  </div>
</template>

<script>
export default {
  name: 'app'
}
</script>

<style>
@import url(http://fonts.googleapis.com/css?family=Roboto:400,100,300,500);
*,
*:before,
*:after {
  box-sizing: inherit;
}

html {
  height: 100%;
  box-sizing: border-box;
}


body {
  background: #fdfdf5!important;
  position: relative;
  margin: 0;
  padding-bottom: 6rem;
  font-family: 'Roboto', sans-serif;
}


h1, h2, h3, h4, p, span, div, small {
  font-family: 'Roboto', sans-serif !important;
}

.reset-margin {
  margin: 0;
}

.row {
  max-width: initial !important;
  padding: 0 .4em;
}

.relative {
  position: relative;
}

.header {
  border-bottom: 1px solid #eaeaea;
  padding-bottom: .5em;
}

.button.alternative--button {
  border: 1px solid #eaeaea;
  background-color: transparent;
  color: #2c3e50;
}

.button.active--button {
  border: 1px solid #eaeaea;
  background-color: #2c3e50;
  color: #fff;
}

.button.active--button:hover,  .button.active--button:focus, .button.active--button:active{
  border: 1px solid #eaeaea;
  background-color: #3b5269;
  color: #fff;
}

.button.alternative--button:hover, .button.alternative--button:active,
.button.alternative--button:focus, .button.alternative--button:visited {
  background-color: #2c3e50;
  color: #fff;
}

.new--file {
  color: #2c3e50;
  position: relative;
  top: 10px;
  right: 10px;
}

.new--file:hover, .new--file:visited, .new--file:focus {
  color: #4e6983;
}

.header--text {
  padding-bottom: .4em;
}

.header--text--title {
  font-size: 3em;
  color: #2c3e50;
  margin: 0;
}


.header--text--title__support {
  color: #7f8c8d;
  width: 100%;
  display: block;
  text-align: left;
  font-size: 1.4em;
  position: absolute;
  bottom: 0;
  left: 170px;
}

.header--nav {
    position: relative;
    top: 18px;
}

#app {
  font-family: 'Roboto', sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  height: 100%;
}
</style>
