(function () {
    'use strict';

    // Component representing main navigation control / search bar.
    // This is decoupled from the list component to facilitate consistent
    // high level navigation / search functionality were additional views
    // constructed (e.g. detailed book view)

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
        // When user clicks search button, navigate to list component to view results
        $location.path('/list/').search({query: $scope.searchText});
      };
    }
})();
