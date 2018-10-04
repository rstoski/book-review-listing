(function () {
    'use strict';

    // Main search results list view component. Handles search operation and
    // display/sorting of results. No grid component is used, result columns
    // are simply formatted with a basic boostrap layout. In a real application,
    // a component such as ag-grid could be utilized to avoid reinventing the
    // wheel. Given the limited functionality required for the grid, a simple
    // boostrap layout suffices. No visual feedback is provided to the user on
    // sort operations, as UX is listed as a non-requirement. This could be
    // achieved through many means (css class) but is omitted for time.
    //
    // Pagination is basic, and does not take into account selected sort field
    // or direction, as this is not supposed by the Goodreads API, which
    // always returns results sorted by popularity. Sort across all records
    // would require retrieving all results for search from Goodreads, which
    // is assumed to not be the desired functionality.

    angular.module('bookreview')
        .component('bookList', {
            controller: BookListController,
            templateUrl: 'scripts/app/book-list.html',
            bindings: {
            }
        })
        .config(configRouter);

    BookListController.$inject = ['$scope', '$http', '$location'];

    function BookListController($scope, $http, $location) {
        var vm = this;
        vm.page = 1;

        vm.$onInit = function() {
        };

        // Monitor search param changes to trigger new search
        $scope.$watch(function() {
          return $location.search();
        }, function() {
          vm.updateBooks();
        });

        // Call search mapping with page/query and parse results. 
        // Also resets sort to title-ascending.
        vm.updateBooks = function() {
          vm.query = $location.search().query;
          vm.result = null;
          vm.showLoading = true;
          $http.get('/search', {params: {query: vm.query, page: vm.page}})
            .success(function(data) {
              vm.showLoading = false;
              vm.result = data;
              vm.showPages = data.numPages > 1;
              vm.numPages = parseInt(data.numPages);
              vm.page = parseInt(data.page);
              vm.sortType = '';
              vm.doSort('title');
            })
            .error(function(data) {
              vm.showLoading = false;
            });
        }

        // Update sort based on specified type. If type does not change,
        // sort direction is changed.
        vm.doSort = function(sortType) {
          if (vm.sortType == sortType) {
            vm.sortDir = -1 * vm.sortDir;
          } else {
            vm.sortType = sortType;
            vm.sortDir = 1;
          }
          vm.sort();
        }

        // Updates the search results sort order based on the currently
        // specified sort settings.
        vm.sort = function() {
          if (vm.result.books) {
            vm.result.books.sort(function(a, b) {
              return vm.sortDir * ((vm.sortType == 'title') ?
                a.title.localeCompare(b.title) : a.author.localeCompare(b.author));
            });
          }
        }

        // Update selected page and navigate. Could perform sanity check
        // (higher/lower page does exist) before navigation. Demo assumes
        // non-malicious user and boundary checks handled by ng-disable on
        // page buttons. Avoid spamming of buttons by disabling paging when
        // search is being performed.
        vm.goPage = function(page) {
          if (vm.showLoading) return;
          vm.page = page;
          vm.updateBooks();
        }
    }

    configRouter.$inject = ['$routeProvider'];

    function configRouter($routeProvider) {
        $routeProvider.when('/list/',
        {
            template: '<book-list></book-list>',
            resolve: {
            },
            reloadOnSearch: false
        });
    }
})();
