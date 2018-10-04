(function () {
    'use strict';

    angular.module('bookreview')
        .component('mainNav', {
            controller: MainNavController,
            templateUrl: 'scripts/app/main-nav.html',
            bindings: {
            }
        });

    MainNavController.$inject = ['$scope', '$http', '$location'];

    function MainNavController($scope, $http, $location) {
      var vm = this;

      vm.$onInit = () => {
      };

      vm.findBooks = () => {
        $location.path('/list/').search({query: $scope.searchText});
      };
    }
})();
