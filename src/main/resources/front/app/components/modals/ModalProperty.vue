<template>
  <div class="modal-mask" @click="close(false)" transition="modal">
      <div class="modal-container" @click.stop>
        <div class="modal-header">
          <h3>New property</h3>

          <form>
            <div class="row">
              <div class="large-12 columns">
                <label>Property
                  <input type="text" class="lang-field" placeholder="openppm.sample" v-model="field.name"/>
                </label>
              </div>
            </div>

            <div class="row">
              <div class="large-12 columns" v-for="lang in languages">
                <label>{{ lang }}
                  <input type="text" placeholder="Sample text"  v-model="field.languages[lang]"/>
                </label>
              </div>
            </div>
          </form>

          <div class="modal-footer text-right">
              <button class="button active--button" @click="addProperty" :disabled="!field.name">Add property</button>
          </div>
        </div>
      </div>
  </div>
</template>

<script>
export default {
  name: 'modalProperty',
  props: ['propertiesArray', 'languages'],
  beforeMount() {
    for (var i = 0; i < this.languages.length; i++) {
      this.field.languages[this.languages[i]] = '';
    }
  },

  data: () => {
    return {
      field: {
        name: '',
        languages: {}
      }
    }
  },

  watch: {
    'field.name': function() {
      this.field.name = this.field.name.replace(/\s/g, '.')
    }
  },

  methods: {
    close: function () {
      this.$emit('closeProperty')
    },

    addProperty() {
      var newObject = Object.assign({}, this.field);
      this.$emit('addProperty', newObject)
      this.close();
      this.field.name = '';
    },


  }
}
</script>
