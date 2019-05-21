const proxy = [
    {
      context: '/tweets/api/v1/dashboards',
      target: 'http://localhost:8080'
    }
  ];
  module.exports = proxy;
