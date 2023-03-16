<template>
<div class="side"></div>
</template>

<script>
export default {
  name: "SideComponent"
}
</script>

<style scoped>
.side {
  align-self: stretch;
  background-color: var(--buff);
  width: 10%;
  margin: 10px 0;
  padding: 20px;
  border-radius: 5px;
}
@media screen and (max-width: 480px) {
  .side {
    display: none;
  }
}
</style>
