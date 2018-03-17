var app = angular.module('app', ['ui.router']);

app.config(['$stateProvider', function($stateProvider) {
  var homeState = {
    name: 'home',
    url: '/home',
    templateUrl: 'templates/home.html'
  };

  var aboutState = {
    name: 'about',
    url: '/about',
    templateUrl: 'templates/about.html'
  };

  var factCheckState = {
    name: 'fact-check',
    url: '/fact-check',
    templateUrl: 'templates/fact-check.html'
  };

  var crawlerState = {
    name: 'crawler',
    url: '/crawler',
    templateUrl: 'templates/crawler.html'
  };

  var squirrelState = {
    name: 'squirrel',
    url: '/squirrel',
    templateUrl: 'templates/squirrel.html'
  };

  $stateProvider.state(homeState);
  $stateProvider.state(aboutState);
  $stateProvider.state(factCheckState);
  $stateProvider.state(crawlerState);
  $stateProvider.state(squirrelState);
}]);

app.config(['$urlRouterProvider', function($urlRouterProvider) {  
  $urlRouterProvider.when('', '/home');
  $urlRouterProvider.otherwise('/home');
}])