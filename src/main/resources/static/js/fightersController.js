angular.module('ufcManager')
  .controller('fightersController', function($scope, $localStorage, $http, authService) {


    authService.initUser($scope);

    $scope.logout = function () {
      authService.clearUser($scope);
      window.location.href = 'login.html';
    };

    /* ---------- текущий пользователь (демо) ---------- */
    $scope.mode = 'list';
    $scope.$storage = $localStorage;
    $scope.fighter  = {};
    $scope.fighters = [];
    $scope.weightClasses = [];
    $scope.selectedWeightClass = 'All';
    $scope.onlyActive = 'true'




    $scope.username  = 'test_manager';      // пустая строка, если не вошёл
    $scope.userRole  = 'MANAGER';           // GUEST | MANAGER | ADMIN



    /* --------- утилиты для header --------- */
    $scope.isGuest   = () => $scope.userRole === 'GUEST'  || !$scope.userRole;
    $scope.isManager = () => $scope.userRole === 'MANAGER';
    $scope.isAdmin   = () => $scope.userRole === 'ADMIN';

    $scope.$storage = $localStorage;          // <- сервис доступен в шаблоне
    $scope.fighter  = {};

    $scope.fighters = [     //  MOCK initialization
      {
        id: 1,
        name: 'Jon Jones',
        weightClass: 'Heavyweight',
        country: 'USA',
        ufcRecord: '20-1',
        mmaRecord: '27-1',
        status: 'Active'
      },
      {
        id: 2,
        name: 'Islam Makhachev',
        weightClass: 'Lightweight',
        country: 'Russia',
        ufcRecord: '14-1',
        mmaRecord: '24-1',
        status: 'Active'
      },
      {
        id: 3,
        name: 'Charles Oliveira',
        weightClass: 'Lightweight',
        country: 'Brazil',
        ufcRecord: '21-9',
        mmaRecord: '34-9',
        status: 'Released'
      }
    ];


    $scope.weightClasses = [...new Set($scope.fighters.map(f=>f.weightClass))];

    //  End of mock




        // ===== ЗАГРУЗКА БОЙЦОВ С СЕРВЕРА =====
        // $http.get('/api/fighters')
        //   .then(function(response) {
        //     $scope.fighters = response.data;
        //     $scope.weightClasses = [...new Set($scope.fighters.map(f => f.weightClass))];
        //     regroup();
        //   })
        //   .catch(function(error) {
        //     console.error("Error loading fighters:", error);
        //   });

        // ===== СОХРАНЕНИЕ БОЙЦА (CREATE или UPDATE) =====
        // $http.post('/api/fighters', $scope.fighter)
        //   .then(function(response) {
        //     // Если новый
        //     $scope.fighters.push(response.data);
        //     regroup();
        //     $scope.cancelEdit();
        //   })
        //   .catch(function(error) {
        //     console.error("Error creating fighter:", error);
        //   });

        // $http.put('/api/fighters/' + $scope.fighter.id, $scope.fighter)
        //   .then(function(response) {
        //     // Если обновление
        //     const idx = $scope.fighters.findIndex(f => f.id === $scope.fighter.id);
        //     if (idx !== -1) {
        //       $scope.fighters[idx] = response.data;
        //       regroup();
        //       $scope.cancelEdit();
        //     }
        //   })
        //   .catch(function(error) {
        //     console.error("Error updating fighter:", error);
        //   });

        // ===== УДАЛЕНИЕ БОЙЦА =====
        // $http.delete('/api/fighters/' + $scope.fighter.id)
        //   .then(function() {
        //     $scope.fighters = $scope.fighters.filter(f => f.id !== $scope.fighter.id);
        //     regroup();
        //     $scope.cancelEdit();
        //   })
        //   .catch(function(error) {
        //     console.error("Error deleting fighter:", error);
        //   });


    function regroup() {
          let arr = $scope.fighters.slice();

          if ($scope.onlyActive === 'true')  arr = arr.filter(f => f.status === 'Active');
          if ($scope.onlyActive === 'false') arr = arr.filter(f => f.status === 'Released');
          if ($scope.selectedWeightClass !== 'All') {
                arr = arr.filter(f => f.weightClass === $scope.selectedWeightClass);
          }
          const grouped = {};
          arr.forEach(f => (grouped[f.weightClass] ||= []).push(f));
          $scope.filtered = grouped;                // <── HTML response
    }

    regroup();                                      // initial view - mock

    $scope.$watchGroup(['selectedWeightClass','onlyActive','fighters'], regroup, {deep:true});


    $scope.addFighter = () => {
        $scope.fighter = {status:'Active'};
        $scope.mode    = 'edit';
    };

    $scope.editFighter = f => {
        $scope.fighter = angular.copy(f);
        $scope.mode    = 'edit';
    };


    $scope.saveFighter = () => {
        if ($scope.fighter.id) {                        // update - MOCK - take from http
          const idx = $scope.fighters.findIndex(x=>x.id===$scope.fighter.id);
          if (idx!==-1) $scope.fighters[idx] = angular.copy($scope.fighter);
        } else {                                        // create - MOCK - take from http
          $scope.fighter.id = Date.now();               // simple generation of id
          $scope.fighters.push(angular.copy($scope.fighter));
          $scope.weightClasses = [...new Set($scope.fighters.map(f=>f.weightClass))];
        }
        $scope.cancelEdit();         // MOCK
    };


    $scope.deleteFighter = function () {
      if (confirm('Delete ' + $scope.fighter.name + '?')) {
        $scope.fighters = $scope.fighters.filter(f => f.id !== $scope.fighter.id);     // MOCK - take from http
        $scope.cancelEdit();
      }
    };

    $scope.cancelEdit = function () {
      $scope.fighter = {};
      $scope.mode    = 'list';
    };







  });


