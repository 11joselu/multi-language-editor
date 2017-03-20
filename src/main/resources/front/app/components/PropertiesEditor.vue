<template>
    <span contenteditable  @blur="updateValue" :class="{'is-empty': isEmptyValue()}">{{property.languages[lang]}}</span>
</template>

<script>
  export default {
    name: 'propertiesEditor',
    props: ['lang', 'property'],
    mounted() {
      // issue https://github.com/vuejs/vue/issues/5234
      if (this.property.languages[this.lang] === "") {
        this.property.languages[this.lang] = '  ';
      }

    },
    methods: {
      updateValue($event) {
        $event.preventDefault();
        this.property.languages[this.lang] = $event.target.innerText;
      },

      isEmptyValue() {
        return !this.property.languages[this.lang].trim()
      }
    }
  }
</script>

<style>
  .is-empty {
    background-color: #fff8bb !important;
  }
</style>


<!--
<template>
  <span contenteditable @input="updateValue" :class="{'is-empty': isEmptyValue()}"></span>
</template>

<script>
  export default {
    name: 'propertiesEditor',
    props: ['lang', 'property'],
    methods: {
      updateValue($event) {
        this.property.languages[this.lang] = $event.target.innerText;
      },

      isEmptyValue() {
        return !this.property.languages[this.lang]
      }
    }
  }
</script>

<style>
  .is-empty {
    background-color: #fff8bb !important;
  }
</style>
-->
