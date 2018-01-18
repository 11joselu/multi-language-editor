<template>
  <div class="modal-mask mask-transparent" @click="close(false)" transition="modal">
      <div class="modal-container" @click.stop>
        <div class="modal-header">
          <h3 class="text-center">Hey!</h3>
          <h4 class="text-center">Did you lose some properties?</h4>
          <p class="text-center">So... Do you want to recover it?</p>
          <div class="modal-footer text-right">
              <button class="button secondary" @click="close('close')">Cancel</button>
              <button class="button active--button" @click="close('restore')">Restore</button>
          </div>
        </div>
      </div>
  </div>
</template>

<script>
export default {
  name: 'restore-modal',
  data: () => {
    return {

    }
  },
  methods: {
    close: function (eventName) {
      this.$emit(eventName)
    },
  }
}
</script>

<style>
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

  .mask-transparent {
    background-color: transparent !important;
  }
</style>
