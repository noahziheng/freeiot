module.exports = {
  /**
   * Application configuration section
   * http://pm2.keymetrics.io/docs/usage/application-declaration/
   */
  apps : [

    // First application
    {
      name      : 'FreeIOT-API',
      script    : 'index.js',
      env: {
        COMMON_VARIABLE: 'true'
      },
      env_production : {
        NODE_ENV: 'production',
        PORT: 2335
      }
    }
  ],

  /**
   * Deployment section
   * http://pm2.keymetrics.io/docs/usage/deployment/
   */
  deploy : {
    production : {
      user : 'root',
      host : '121.42.231.226',
      ref  : 'origin/master',
      repo : 'git@github.com:noahziheng/freeiot-api.git',
      path : '/data/www/freeiot-api',
      'post-deploy' : 'cp config.js.sample config.js && yarn install && pm2 reload ecosystem.config.js --env production'
    },
    dev : {
      user : 'root',
      host : '121.42.231.226',
      ref  : 'origin/master',
      repo : 'git@github.com:noahziheng/freeiot-api.git',
      path : '/data/www/freeiot-api',
      'post-deploy' : 'yarn install && pm2 reload ecosystem.config.js --env dev',
      env  : {
        NODE_ENV: 'dev'
      }
    }
  }
};
