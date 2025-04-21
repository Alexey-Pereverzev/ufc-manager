angular.module('ufcManager')
  .controller('rankingController', function($scope, authService) {

    authService.initUser($scope);

    $scope.logout = function () {
      authService.clearUser($scope);
      window.location.href = 'login.html';
    };

    $scope.username = 'john_doe'; // example
    $scope.role = 'MANAGER';      // GUEST | MANAGER | ADMIN

    $scope.isAdmin   = () => $scope.role === 'ADMIN';
    $scope.isManager = () => $scope.role === 'MANAGER';
    $scope.isGuest   = () => !$scope.role || $scope.role === 'GUEST';
    $scope.logout    = () => { $scope.username=''; $scope.role=''; };

    $scope.weightClasses = [];
    $scope.selectedClass = '';
    $scope.rankings = {};

    /* ======== HTTP-запросы (закомментированы для примера) ======== */

    // Загрузка всех весовых категорий + добавление виртуальных категорий
    // $http.get('/api/weight-classes').then(function(response) {
    //   $scope.weightClasses = response.data;
    //   // Добавляем спецкатегории вручную
    //   $scope.weightClasses.unshift(          // add to the top of the list
    //     { name: "All" },
    //     { name: "Women's P4P" },
    //     { name: "Men's P4P" }
    //   );
    //   $scope.selectedClass = $scope.weightClasses[0]; // по умолчанию первая
    // }).catch(function(error) {
    //   console.error("Error fetching weight classes:", error);
    // });

    // Загрузка рейтингов по выбранной категории
    // $scope.loadRankings = function() {
    //   const category = encodeURIComponent($scope.selectedClass);
    //   $http.get('/api/rankings?category=' + category)
    //     .then(function(response) {
    //       $scope.rankings[$scope.selectedClass] = response.data;
    //     })
    //     .catch(function(error) {
    //       console.error("Error fetching rankings:", error);
    //     });
    // };

    // Весовые категории
    $scope.weightClasses = [
      "All",
      "Men's P4P",
      "Women's P4P",
      'Flyweight', 'Bantamweight', 'Featherweight', 'Lightweight',
      'Welterweight', 'Middleweight', 'Light Heavyweight', 'Heavyweight',
      "Women's Strawweight", "Women's Flyweight", "Women's Bantamweight"
    ];

    $scope.selectedClass = "Men's P4P";

    // Моки рейтингов
    $scope.rankings = {
      "Men's P4P": [
        { name: 'Islam Makhachev', score: 2702.1, rank: 0},
        { name: 'Charles Oliveira', score: 2675.9, rank: 1 },
        { name: 'Dustin Poirier', score: 2640.2, rank: 2 },
        { name: 'Justin Gaethje', score: 2602.0, rank: 3 },
        { name: 'Michael Chandler', score: 2587.1, rank: 4 },
        { name: 'Beneil Dariush', score: 2573.0, rank: 5 },
        { name: 'Rafael Fiziev', score: 2555.4, rank: 6 },
        { name: 'Mateusz Gamrot', score: 2522.2, rank: 7 },
        { name: 'Arman Tsarukyan', score: 2504.7, rank: 8 },
        { name: 'Dan Hooker', score: 2489.9, rank: 9 },
        { name: 'Renato Moicano', score: 2473.2, rank: 10 },
        { name: 'Jalin Turner', score: 2456.8, rank: 11 },
        { name: 'Grant Dawson', score: 2442.0, rank: 12 },
        { name: 'Thiago Moises', score: 2420.1, rank: 13 },
        { name: 'Damir Ismagulov', score: 2402.7, rank: 14 },
        { name: 'Drew Dober', score: 2389.3, rank: 15 },
        { name: 'Bobby Green', score: 2370.5, rank: 16 }
      ],
      "Women's P4P": [
        { name: 'Zhang Weili', score: 2500.0, rank: 0 },
        { name: 'Alexa Grasso', score: 2475.2, rank: 1 },
        { name: 'Valentina Shevchenko', score: 2450.3, rank: 2 }
      ]
      // You can add more categories as needed...
    };

  /* выбор категории – никакой логики не нужно, Angular сам обновит вид  */
    $scope.selectWeightClass = () => {};

    $scope.getVisibleRankings = function () {
      const list = $scope.rankings[$scope.selectedClass] || [];
      return $scope.isGuest() ? list.slice(0, 16) : list;
    };

});

