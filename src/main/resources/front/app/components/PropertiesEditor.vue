<template>
    <span contenteditable  @paste="pasteValue" @blur="updateValue"
          :class="{'is-empty': isEmptyValue()}" :inner-text.prop="property.languages[lang]"></span>
</template>

<script>
  export default {
    name: 'propertiesEditor',
    props: ['lang', 'property'],
    mounted() {
      if (this.property.languages[this.lang].trim() === "") {
        this.resetValue();
      }

    },
    methods: {
      updateValue($event) {
        $event.preventDefault();
        $event.stopPropagation();

        if ($event.target.innerText.trim()) {
          this.property.languages[this.lang] = $event.target.innerText;
        }

        if (this.isEmptyValue()) {
          this.resetValue();
        }
      },

      isEmptyValue() {
        return !this.property.languages[this.lang].trim()
      },

      pasteValue(e) {
        e.stopPropagation();
        e.preventDefault();

        // Get pasted data via clipboard API
        var clipboardData = e.clipboardData || window.clipboardData;
        var pastedData = clipboardData.getData('Text');
        if (pastedData) {
          this.property.languages[this.lang] = pastedData;
        }

        if (this.isEmptyValue()) {
          this.resetValue();
        }
      },

      resetValue() {
        // issue https://github.com/vuejs/vue/issues/5234
        this.property.languages[this.lang] = '  ';
      }
    }
  }
</script>

<style>
  .is-empty {
    background-color: #fff8bb !important;
  }
</style>
