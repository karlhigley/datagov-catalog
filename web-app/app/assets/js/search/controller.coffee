define([
  'angular'
  ], (angular) ->

  class SearchCtrl
    constructor: (@scope, @http) ->
      @search("")

    search: (query) =>
      @http.get('/results').
        success( (data, status, headers, config) =>
          @scope.datasets = data
        )

  ['$scope', '$http', SearchCtrl]
)