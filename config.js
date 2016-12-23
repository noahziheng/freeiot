const config = {
  environment: process.env.NODE_ENV || 'dev',
  server: {
    port: process.env.PORT || 8080
  },
  mongo: {
    url: process.env.MONGO_DB_URI || 'mongodb://localhost/freeiot-api'
  },
  mqtt: {
    server: process.env.MQTT_SERVER || 'mqtt',
    port: process.env.MQTT_PORT || '1883'
  },
  key: {
    jwt: '1e6f17c8b92bd24568950e0afbaa1cf2',
    pass: '89c3e3db9f580cb4b2b4c3fa00f0f13a'
  }
}

module.exports = config
