angular.module('ufcManager')
  .controller('mainController', function($scope, $localStorage, $http, authService) {
    console.log("mainController loaded");

    authService.initUser($scope);

    $scope.logout = function () {
      authService.clearUser($scope);
      window.location.href = 'login.html';
    };

    $scope.isAdmin   = () => $scope.role === 'ADMIN';
    $scope.isManager = () => $scope.role === 'MANAGER';
    $scope.isGuest   = () => !$scope.role || $scope.role === 'GUEST';

//    $scope.role = 'ADMIN';

    // =============================
    // Mock data for now (until backend is implemented)
    // =============================

    $scope.topP4P = [
      { name: 'Jon Jones', weightClass: 'Heavyweight', rating: 2750.4 },
      { name: 'Islam Makhachev', weightClass: 'Lightweight', rating: 2702.1 },
      { name: 'Leon Edwards', weightClass: 'Welterweight', rating: 2671.3 },
      { name: 'Alex Pereira', weightClass: 'Light Heavyweight', rating: 2645.0 },
      { name: 'Sean O\'Malley', weightClass: 'Bantamweight', rating: 2612.6 },
      { name: 'Ilia Topuria', weightClass: 'Featherweight', rating: 2598.9 },
      { name: 'Aljamain Sterling', weightClass: 'Featherweight', rating: 2567.7 },
      { name: 'Charles Oliveira', weightClass: 'Lightweight', rating: 2549.3 },
      { name: 'Dustin Poirier', weightClass: 'Lightweight', rating: 2533.2 },
      { name: 'Robert Whittaker', weightClass: 'Middleweight', rating: 2511.5 }
    ];

    const allTournaments = [
      { name: 'UFC 300', date: '30.06.2025', location: 'Las Vegas' },
      { name: 'UFC Fight Night', date: '05.07.2025', location: 'New York' },
      { name: 'UFC 301', date: '12.07.2025', location: 'London' },
      { name: 'UFC 302', date: '19.07.2025', location: 'Toronto' },
      { name: 'UFC 303', date: '26.07.2025', location: 'Dubai' },
      { name: 'UFC 304', date: '02.08.2025', location: 'Paris' }
    ];

    $scope.upcomingTournaments = allTournaments.slice(0, 5);

    // =============================
    // Real API fetch (commented until backend is ready)
    // =============================

    // $http.get('/api/rankings/p4p')
    //   .then(function(response) {
    //     $scope.topP4P = response.data.slice(0, 10); // or all, depending on endpoint
    //   })
    //   .catch(function(error) {
    //     console.error("Error fetching top P4P data:", error);
    //   });

    // $http.get('/api/tournaments/upcoming')
    //   .then(function(response) {
    //     $scope.upcomingTournaments = response.data.slice(0, 5);
    //   })
    //   .catch(function(error) {
    //     console.error("Error fetching upcoming tournaments:", error);
    //   });
  });