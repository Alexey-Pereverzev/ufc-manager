// js/mainController.js
angular.module('ufcManager')
  .controller('mainController', function($scope, $localStorage, $http, authService) {
    console.log("mainController loaded");

    // Инициализация пользователя
    authService.initUser($scope);

    // Методы авторизации
    $scope.logout = function () {
      authService.clearUser($scope);
      window.location.href = 'login.html';
    };


    $scope.isUnauthorized = () => authService.isUnauthorized();
    $scope.isGuest        = () => $scope.role === 'GUEST';
    $scope.isManager      = () => $scope.role === 'MANAGER';
    $scope.isAdmin        = () => $scope.role === 'ADMIN';

    // Загружаем Top-10 P4P
    $http.get('/api/rankings?category=men\'s%20p4p')
      .then(function(response) {
        $scope.topP4P = response.data
          .sort((a, b) => b.score - a.score)
          .slice(0, 10);
      })
      .catch(function(error) {
        console.error("Ошибка загрузки рейтинга P4P:", error);
        $scope.topP4P = [];
      });

    // Загрузка последних турниров
    $http.get('/api/tournaments/recent')
      .then(function(response) {
        $scope.upcomingTournaments = response.data.slice(0, 5); // показываем последние 5
      })
      .catch(function(error) {
        console.error("Ошибка при загрузке турниров:", error);
        $scope.upcomingTournaments = []; // fallback
      });
  });




  //    $scope.role = 'ADMIN';

      // =============================
      // Mock data for now (until backend is implemented)
      // =============================

//      $scope.topP4P = [
//        { name: 'Jon Jones', weightClass: 'Heavyweight', rating: 2750.4 },
//        { name: 'Islam Makhachev', weightClass: 'Lightweight', rating: 2702.1 },
//        { name: 'Leon Edwards', weightClass: 'Welterweight', rating: 2671.3 },
//        { name: 'Alex Pereira', weightClass: 'Light Heavyweight', rating: 2645.0 },
//        { name: 'Sean O\'Malley', weightClass: 'Bantamweight', rating: 2612.6 },
//        { name: 'Ilia Topuria', weightClass: 'Featherweight', rating: 2598.9 },
//        { name: 'Aljamain Sterling', weightClass: 'Featherweight', rating: 2567.7 },
//        { name: 'Charles Oliveira', weightClass: 'Lightweight', rating: 2549.3 },
//        { name: 'Dustin Poirier', weightClass: 'Lightweight', rating: 2533.2 },
//        { name: 'Robert Whittaker', weightClass: 'Middleweight', rating: 2511.5 }
//      ];
//
//      const allTournaments = [
//        { name: 'UFC 300', date: '30.06.2025', location: 'Las Vegas' },
//        { name: 'UFC Fight Night', date: '05.07.2025', location: 'New York' },
//        { name: 'UFC 301', date: '12.07.2025', location: 'London' },
//        { name: 'UFC 302', date: '19.07.2025', location: 'Toronto' },
//        { name: 'UFC 303', date: '26.07.2025', location: 'Dubai' },
//        { name: 'UFC 304', date: '02.08.2025', location: 'Paris' }
//      ];
//
//      $scope.upcomingTournaments = allTournaments.slice(0, 5);