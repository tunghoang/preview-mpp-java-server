import axios from "axios";

class APIService {
  constructor() {
    this.convertAPI = axios.create({
      baseURL: "http://localhost:8080/",
      headers: {
        "Content-type": "application/json",
      }
    });

    this.tableImageAPI = axios.create({
      baseURL: "http://localhost:8081/",
      headers: {
        "Content-type": "application/json",
      }
    })
  }

}

export default new APIService();
