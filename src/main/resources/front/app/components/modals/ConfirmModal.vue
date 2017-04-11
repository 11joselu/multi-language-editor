<template>
  <div class="modal-mask" @click="close(false)" transition="modal">
      <div class="modal-container" @click.stop>
        <div class="modal-header">
          <h3>Are you sure?</h3>
          <p>Are you sure that you want to delete?</p>
          <div class="modal-footer text-right">
              <button class="button secondary" @click="close(false)">Cancel</button>
              <button class="button active--button" @click="close(true)">Remove</button>
          </div>
        </div>
      </div>
  </div>
</template>

<script>
export default {
  name: 'confirmModal',
  data: () => {
    return {

    }
  },
  methods: {
    close: function (remove) {
      this.$emit('closeConfirm', remove)
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
</style>
