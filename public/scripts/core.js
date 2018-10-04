var bcCheckin = angular.module('bookreview', ['ngRoute'])
  .controller('mainController', MainController);

function init($scope, $http, $location) {
}

function MainController($scope, $http, $location) {
  $scope.formData = {};

  init($scope, $http, $location);
}
