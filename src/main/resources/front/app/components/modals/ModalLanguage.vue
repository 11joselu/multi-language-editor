<template>
  <div class="modal-mask" @click="close" transition="modal">
      <div class="modal-container" @click.stop>
        <div class="modal-header">
                <h3>New language</h3>
        </div>

            <form>
              <div class="row">
                <div class="large-12 columns">
                  <label :class="{error: showMessage}">Language <small>(File name)</small>
                    <input type="text" class="lang-field" placeholder="E.g 'US'" v-model="field.lang"/>
                       <small v-if="showMessage" class="error">Yo!, '{{field.lang}}' already exists</small>
                    </span>
                  </label>
                </div>
              </div>

              <div class="row">
                <div class="large-12 columns">
                  <label>Copy from
                    <select v-model="field.copy">
                      <option v-for="lang in languages" :value="lang">{{lang}}</option>
                    </select>
                  </label>
                </div>
              </div>

            </form>

            <div class="modal-footer text-right">
                <button class="button active--button" @click="addToArray" :disabled="!this.field.lang">
                  Add new language
                </button>
            </div>
      </div>
  </div>
</template>

<script>
export default {
  name: 'modalLanguage',
  props: ['propertiesArray', 'languages'],
  data: () => {
    return {
      field: {
        lang: '',
        copy: ''
      },
      showMessage: false
    }
  },
  methods: {
        close: function () {
            this.$emit('close')
        },

        addToArray: function() {
          this.$emit('add', this.field.lang, this.field.copy);
          this.close();
        },

        validateLanguage: function() {
          if (this.languages.indexOf(this.field.lang) >= 0) {
            this.showMessage = true;
          } else {
            this.showMessage = false;
          }
        }
  },
  watch: {
    'field.lang': function(val, oldVal){
      this.validateLanguage();
    }
  }
}
</script>

<style>
.modal-mask {
  position: fixed;
  z-index: 9998;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, .5);
  display: table;
  transition: opacity .3s ease;
  top: 50%;
  transform: translateY(-50%);
}

.modal-wrapper {
  display: table-cell;
  vertical-align: middle;
}

.modal-container {
  max-width: 600px;
  margin: 0px auto;
  padding: 20px 30px;
  background-color: #fff;
  border-radius: 2px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, .33);
  transition: all .3s ease;
  transform: translateY(20%);
  max-height: 600px;
  overflow: auto;
}

.modal-header h3 {
  margin-top: 0;
}

.modal-body {
  margin: 20px 0;
}

.modal-default-button {
  float: right;
}

/*
 * The following styles are auto-applied to elements with
 * transition="modal" when their visibility is toggled
 * by Vue.js.
 *
 * You can easily play with the modal transition by editing
 * these styles.
 */

.modal-enter {
  opacity: 0;
}

.modal-leave-active {
  opacity: 0;
}

.modal-enter .modal-container,
.modal-leave-active .modal-container {
  -webkit-transform: scale(1.1);
  transform: scale(1.1);
}

.lang-field {
  margin: 0 !important;
}
small.error {
    display: block;
    font-size: 0.66667rem;
    font-style: italic;
    font-weight: normal;
    margin-bottom: 0.88889rem;
    margin-top: -1px;
    padding: 0.33333rem 0.5rem 0.5rem;
    background: #f04124;
    color: #FFFFFF;
}
</style>
