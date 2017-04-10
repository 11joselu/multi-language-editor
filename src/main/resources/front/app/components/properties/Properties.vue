<template>
  <div v-if="properties && properties.length > 0">
    <div class="row">
      <div class="large-12 columns text-right properties--button">
        <button class="button success" @click="downloadZipFile">Download as ZIP</button>
      </div>
    </div>
    <div class="text-right properties--button row">
      <div class="small-6 large-6 columns text-left">
        <button class="button secondary" @click="showPropertyModal = true">Add new property</button>
        <button class="button" @click="showModal = true">Add new language</button>
      </div>
      <div class="small-6 large-6 columns">
        <button id="show-modal" class="button alert" :disabled="columnsSelected.length == 0"
                @click="activeDeleteButton=true">Delete Columns
        </button>
      </div>

    </div>


    <div id="properties" class="properties row">
      <table class="properties--table fixed--header">
        <thead>
          <th>#</th>
          <th class="text-center alternative--bg">Property</th>
          <th class="text-center header--selectable no-select" v-for="lang in languages" @click="toggleSelectColumn(lang)"
              :class="{'is-tr-selected': isSelected(lang)}">
            {{lang}}
          </th>
          <th class="text-center alternative--bg"></th>
        </thead>

        <tbody>
          <tr v-for="(property, index) in properties" :class="{ 'is-tr-selected': propertyIsEquals(property) }" :key="index">
            <td class="alternative--bg no-select pointer" @click="toggleProperty(property)">{{index}}</td>
            <td >{{property.nameProperty}}</td>
            <td v-for="lang in languages" :class="{'is-tr-selected': isSelected(lang)}">
              <properties-editor :lang="lang" :property="property"></properties-editor>
            </td>
            <td><i class="fa fa-trash-o fa-2x icon--delete" aria-hidden="true"
                   @click="removeProperty(property, index)"></i></td>
          </tr>
        </tbody>
      </table>
    </div>

    <modal-language v-if="showModal" @close="showModal = false" :properties-array="properties" :languages="languages"
                    @add="mutateArray"></modal-language>
    <modal-property v-if="showPropertyModal" :properties-array="properties" :languages="languages"
                    @closeProperty="showPropertyModal = false"
                    @addProperty="addProperty"
                    ></modal-property>
    <confirm-modal v-if="showConfirm" @closeConfirm="confirmModal"></confirm-modal>
    <confirm-modal v-if="activeDeleteButton" @closeConfirm="deleteColumn"></confirm-modal>
  </div>
</template>

<script>

  import ModalLanguage from '../modals/ModalLanguage'
  import ConfirmModal from '../modals/ConfirmModal'
  import ModalProperty from '../modals/ModalProperty'
  import PropertiesEditor from './PropertiesEditor.vue'
  import * as zip from '../../services/Zip'

  export default {
    name: 'properties',
    components: {
      ModalLanguage,
      ConfirmModal,
      ModalProperty,
      PropertiesEditor
    },

    beforeRouteEnter (to, from, next) {
      var properties = to.params.properties;

      next(vm => {

        if (!properties) {
          vm.$router.go(-1)
          return
        }

        vm.properties = properties;

        vm.languages = Object.keys(properties[0].languages).sort()

        localStorage.removeItem('properties');
      })
    },

    mounted () {
      window.addEventListener("beforeunload", function alertLoad(e) {
        var confirmationMessage = "If you continue, you will lose all your changes";

        (e || window.event).returnValue = confirmationMessage; //Gecko + IE

        return confirmationMessage;  //Webkit, Safari, Chrome
      });
    },

    data () {
      return {
        properties: undefined,
        languages: [],
        showModal: false,
        showConfirm: false,
        columnsSelected: [],
        indexToDelete: -1,
        propertySelected: null,
        activeDeleteButton: false,
        showPropertyModal: false
      }
    },

    methods: {
      mutateArray: function (lang, copyFrom) {
        var vm = this;
        if (this.languages.indexOf(lang) < 0) {
          this.properties = this.properties.map((arr) => {
            var text = '';

            if (copyFrom) {
              text = arr.languages[copyFrom];
            }

            vm.$set(arr.languages, lang, text)

            return arr;
          })

          this.languages.push(lang)
          this.languages.sort();

        }
      },

      toggleSelectColumn(lang) {
        var index = this.columnsSelected.indexOf(lang);
        if (index >= 0) {
          this.columnsSelected.splice(index, 1)
        } else {
          this.columnsSelected.push(lang)
        }

      },


      isSelected(lang) {
        return this.columnsSelected.indexOf(lang) >= 0;
      },

      removeColumn () {
        var vm = this;
        if (this.columnsSelected.length === 0) {
          return;
        }

        this.properties = this.properties.map((property) => {
          for (let i = vm.columnsSelected.length - 1; i >= 0; i--) {
            let langSelected = vm.columnsSelected[i];
            let index = vm.languages.indexOf(langSelected);
            delete property.languages[langSelected]

            if (index >= 0) {
              vm.languages.splice(index, 1);
            }

            vm.columnsSelected.splice(i, 1);

          }

          return property;
        })


      },

      toggleProperty (property) {
        if (this.propertyIsEquals(property, this.propertySelected)) {
          this.propertySelected = null;
        } else {
          this.propertySelected = property;
        }
      },

      propertyIsEquals(property) {

        return JSON.stringify(this.propertySelected) === JSON.stringify(property);
      },

      removeProperty (property, index) {
        this.indexToDelete = index;
        this.showConfirm = true;
      },

      addProperty(field) {
        var newProperty = {
          nameProperty: field.name,
          languages: {}
        };

        for (var language in field.languages) {
          newProperty.languages[language] = field.languages[language];
        }

        this.properties.push(newProperty)

        this.properties.sort((propA, propB) => {
          if (propA.nameProperty.toUpperCase() > propB.nameProperty.toUpperCase()) return 1;
          if (propA.nameProperty.toUpperCase() < propB.nameProperty.toUpperCase()) return -1;
          return 0;
        });
      },

      confirmModal (isConfirmed) {
        if (isConfirmed) {
          this.properties.splice(this.indexToDelete, 1);
          this.indexToDelete = -1;
        }

        this.showConfirm = false;
      },

      deleteColumn(isConfirmed) {
        if (isConfirmed) {
          this.removeColumn()
        }

        this.activeDeleteButton = false;
      },

      downloadZipFile () {
        this.$http.post('/generate', this.properties, {
          params:  {
            languages: this.languages.join(',')
          },
          headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/zip'
          }
        })
          .then(zip.getZIPData)
          .then((link) => {
            link.click();
            document.removeChild(link);
          })
          .catch((err) => {
            throw err;
          })
      }
    }

  }
</script>

<style>

  * {
    outline: none;
  }

  .properties {
    overflow: auto;
    max-height: 700px;
    max-width: 100%;
  }

  .properties--button {
    margin: 1em 1em .3em 0;
  }

  .properties--table {
    box-shadow: 0px 1px 2px #e2e2e2;
  }

  .properties--table th {
    font-weight: normal;
    color: #4d4d4d;
    background: #eaeaea;
  }

  .properties--table td span {
    border: 1px solid transparent;
    margin: 0;
    min-width: 200px;
    background: transparent;
    display: block;
    width: 100%;
    padding: .25em;
    min-height: 1em;
  }

  .properties--table td span:focus, .properties--table td span:active{
    border: 1px solid #358f5e;
  }

  .alternative--bg {
    color: #4d4d4d;
    background: #eaeaea;
  }

  .header--selectable {
    cursor: pointer;
    border: 1px solid transparent;
  }

  .pointer {
    cursor: pointer;
  }

  .no-select {
    user-select: none;
  }

  .header--selectable:hover {
    border: 1px solid #358f5e;
  }

  .icon--delete {
    font-size: 1.3em;
    color: #cc4b37;
    cursor: pointer;
  }


  .is-tr-selected .alternative--bg, th.is-tr-selected, .is-tr-selected {
    background: #d7d7d7 !important
  }

  .button.success {
    color: #fff !important;
    background: #27ae60 !important;
  }

</style>
