<template>
  <div class="container">
    <h2>Find Delivery Fee</h2>
    <Popup :header-msg="this.headerMsg" :body-msg="this.bodyMsg" v-if="showPopup" @close-popup="showPopup=false"></Popup>
    <form @submit.prevent="onSubmit" class="form-container">
      <div>
        <input type="radio" v-model="vehicle" value="Car" name="car">
        <label for="car">CAR</label><br>
        <input type="radio" v-model="vehicle" value="Bike" name="bike">
        <label for="bike">BIKE</label><br>
        <input type="radio" v-model="vehicle" value="Scooter" name="scooter">
        <label for="scooter">SCOOTER</label>
      </div>
      <br>
      <div>
        <select v-model="city">
          <option value="">--Choose a city--</option>
          <option value="Tallinn-Harku">Tallinn</option>
          <option value="Tartu-Tõravere">Tartu</option>
          <option value="Pärnu">Pärnu</option>
        </select>
      </div>
      <button type="submit">CALCULATE</button>
    </form>
  </div>
</template>

<script>
import Popup from "@/components/PopupComponent.vue";

export default {
  name: "FormComponent",
  components: {
    Popup
  },
  data() {
    return {
      showPopup: false,
      headerMsg: "Header message",
      bodyMsg: "Body message",
      vehicle: "",
      city: "",
    }
  },
  methods: {
    async onSubmit() {
      const newInformation = { // Gets user input
        vehicle: this.vehicle,
        city: this.city,
      }

      if (newInformation.vehicle === "") { // Checks if needed user input has been given. If not, then it will be told to the user.
        this.headerMsg = "NOTIFICATION";
        this.bodyMsg = "Add delivery vehicle information!"

      } else if (newInformation.city === "") {
        this.headerMsg = "NOTIFICATION";
        this.bodyMsg = "Add delivery city information!"

      } else {
        let response = await fetch("http://localhost:8080/api/deliveryFee?station=" + newInformation.city + "&vehicle=" + newInformation.vehicle);
        response = await response.text(); // Gets response from the backend

        if (!isNaN(response) && !isNaN(parseFloat(response))) { // If the response is a number then no error occurred
          this.headerMsg = "RESULT" // Delivery fee will be shown to the user and then the input is cleaned.
          this.bodyMsg = "Delivery fee will be " + response + " €."
          this.vehicle = "";
          this.city = "";

        } else { // If the response is a string then error has occurred
          this.headerMsg = "NOTIFICATION" // Error will be shown to the user
          this.bodyMsg = response;
        }
      }
      this.showPopup = true;
    }
  }
}
</script>

<style scoped>
.container {
  display: flex;
  flex-direction: column;
  justify-content: center;
  text-align: center;
  width: 60%;
  min-height: 500px;
  padding: 10px;
  margin: 20px 10px 20px 10px;
  border-radius: 5px;
  background-color: var(--vanilla);
}
.form-container{
  margin: 5px 20px 20px 20px;
}
/* Inspiration for button taken from here - https://copy-paste-css.com/ */
button {
  cursor: pointer;
  font-size: 14px;
  line-height: 1;
  border-radius: 300px;
  border-color: var(--buff);
  min-width: 160px;
  white-space: normal;
  font-weight: 550;
  text-align: center;
  padding: 16px 14px 18px;
  margin: 20px;
  color: white;
  background-color: var(--buff);
  height: 48px;
}
button:hover{
  background-color: var(--old-rose);
}
select {
  background-color: var(--beige);
  border: 6px solid transparent;
}
input[type=radio] {
  accent-color: var(--old-rose);
}
@media screen and (max-width: 480px) {
  button {
    font-size: 12px;
    min-width: 90px;
    margin: 10px;
  }
}
</style>
