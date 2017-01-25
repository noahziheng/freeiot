FROM node:wheezy

ENV APP_HOME /app/
RUN mkdir $APP_HOME

# caching npm packages
RUN echo "Asia/Shanghai" > /etc/timezone
RUN npm config set registry https://registry.npm.taobao.org
RUN npm install -g pm2
WORKDIR $APP_HOME

EXPOSE 80
EXPOSE 1883

CMD ["npm start"]
