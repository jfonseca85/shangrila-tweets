const proxy = [
    {
      context: '/resource/dashboards',
      target: 'http://localhost:8080'
    }
  ];
  module.exports = proxy;