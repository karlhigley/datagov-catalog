requirejs.config(
  paths:
    'angular': '../lib/angularjs/angular',
    'angular-route': '../lib/angularjs/angular-route'
  shim:
    'angular':
      exports: 'angular'
    'angular-route': ['angular']
)

require([
    'angular',
    'angular-route',
    './search/main' 
  ], (angular) ->

  angular.bootstrap(document, ['datagov.search'])
)