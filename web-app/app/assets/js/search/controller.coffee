define([
  'angular'
  ], (angular) ->

  class SearchCtrl
    constructor: (@scope, @http) ->
      @scope.ctrl = @
      @search("")

    search: (query) =>
      @http.get('/results?q=' + query).
        success( (data, status, headers, config) =>
          @scope.datasets = data
        )

  ['$scope', '$http', SearchCtrl]
)