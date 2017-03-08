<template>
  <div id="app container-fluid">
    <header class="header">
      <div class="row">
        <div class="small-8 large-10 columns text-left">
          <p class="reset-margin relative header--text">
            <i class="fa fa-file fa-2x header--text--icon" aria-hidden="true"></i>
            <strong class="header--text--title">Properties</strong>
            <span class="header--text--title__support">Editor</span>
          </p>
        </div>
        <div class="small-4 large-2 columns text-right header--nav">
          <router-link to="home" class="button success">Upload <strong>new file</strong></router-link>
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

.header, .header .button.success {
  background: #358f5e;
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

.header--text {
  padding-bottom: .4em;
}


.header--text--title {
  font-size: 3em;
  color: #fff;
  margin: 0;
}


.header--text--icon {
  color: #fff;
}

.header--text--title__support {
  color: #7bc39c;
  width: 100%;
  display: block;
  text-align: left;
  font-size: 1.4em;
  position: absolute;
  bottom: 0;
  left: 195px;
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

.footer {
  position: absolute;
  right: 0;
  bottom: 0;
  left: 0;
  padding: 1rem;
  text-align: center;
}
</style>
