<template>
  <div class="overlay">
    <div class="container">
      <div>
        <p>{{headerMsg}}</p>
      </div>
      <div>
        <p>{{bodyMsg}}</p>
      </div>
      <div>
        <button @click="$emit('close-popup')" class="main-button">OK</button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "PopupComponent",
  props: {
    headerMsg: String,
    bodyMsg: String,
  }
}
</script>

<style scoped>
.overlay {
  position: fixed;
  top: 0;
  bottom: 0;
  left: 0;
  right: 0;
  display: flex;
  justify-content: center;
  background-color: rgba(28, 18, 18, 0.71);
}
.container {
  position: fixed;
  background-color: var(--old-rose);
  color: white;
  width: 300px;
  height: 200px;
  border-radius: 20px;
  display: flex;
  flex-direction: column;
  justify-content: space-around;
  top: 50%;
  -ms-transform: translateY(-50%);
  transform: translateY(-50%);
}
/* Inspiration for button taken from here - https://copy-paste-css.com/ */
.main-button {
  cursor: pointer;
  font-size: 14px;
  line-height: 1;
  border-radius: 300px;
  border-color: var(--buff);
  min-width: 80px;
  white-space: normal;
  font-weight: 400;
  text-align: center;
  padding: 16px 14px 18px;
  margin: 10px;
  color: white;
  background-color: var(--buff);
  height: 48px;
}
</style>
