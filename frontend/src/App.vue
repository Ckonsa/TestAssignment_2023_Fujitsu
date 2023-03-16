<template>
  <Header></Header>
  <div class="main-container">
    <Side></Side>
    <Form></Form>
    <Side></Side>
  </div>
  <Footer></Footer>
</template>

<script>
import Header from './components/HeaderComponent.vue'
import Footer from './components/FooterComponent.vue'
import Side from "@/components/SideComponent.vue";
import Form from "@/components/FormComponent.vue";

export default {
  name: 'App',
  components: {
    Form,
    Side,
    Footer,
    Header,
  }
}
</script>

<style>
:root {
  --vanilla: #D6CE93;
  --beige: #EFEBCE;
  --buff: #D8A48F;
  --old-rose: #BB8588;
  font-family: Geneva, Helvetica, Arial,sans-serif;
  background-color: beige;
}
.main-container {
  display: flex;
  justify-content: space-between;
  align-content: stretch;
}
@media screen and (max-width: 480px) {
  .main-container {
    justify-content: center;
  }
}
</style>
