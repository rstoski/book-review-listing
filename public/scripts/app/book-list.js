(function () {
    'use strict';

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

        $scope.$watch(function() {
          return $location.search();
        }, function() {
          vm.updateBooks();
        });

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

        vm.doSort = function(sortType) {
          if (vm.sortType == sortType) {
            vm.sortDir = -1 * vm.sortDir;
          } else {
            vm.sortType = sortType;
            vm.sortDir = 1;
          }
          vm.sort();
        }

        vm.sort = function() {
          if (vm.result.books) {
            vm.result.books.sort(function(a, b) {
              return vm.sortDir * ((vm.sortType == 'title') ?
                a.title.localeCompare(b.title) : a.author.localeCompare(b.author));
            });
          }
        }

        vm.nextPage = function() {
          vm.page++;
          vm.updateBooks();
        }

        vm.prevPage = function() {
          vm.page--;
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
            reloadOnSearch: true
        });
    }
})();
