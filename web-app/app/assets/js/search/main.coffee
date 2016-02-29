define([
    'require',
    'angular',
    'angular-route',
    './controller'
  ], (require, angular) ->

  angular
  	.module('datagov.search', [ 'ngRoute' ] )
  	.controller('SearchCtrl', require('./controller'))
  	.config([
      '$routeProvider',
      ($routeProvider) ->
        $routeProvider.when '/search',
          templateUrl: 'templates/search.html',
          controller: 'SearchCtrl'
        $routeProvider.otherwise
          redirectTo: '/search'
    ])

)