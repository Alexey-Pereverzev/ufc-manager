angular.module('ufcManager')
  .controller('fightersController', function($scope, $localStorage, $http, authService) {

    authService.initUser($scope);

    $scope.logout = function () {
      authService.clearUser($scope);
      window.location.href = 'login.html';
    };

    $scope.mode = 'list';
    $scope.$storage = $localStorage;
    $scope.fighter  = {};
    $scope.fighters = [];
    $scope.weightClasses = [];
    $scope.countryList = [];
    $scope.selectedWeightClass = 'All';
    $scope.onlyActive = 'true';

    $scope.isUnauthorized = () => authService.isUnauthorized();
    $scope.isGuest        = () => $scope.role === 'GUEST';
    $scope.isManager      = () => $scope.role === 'MANAGER';
    $scope.isAdmin        = () => $scope.role === 'ADMIN';

    const weightOrder = [
      'Heavyweight', 'LightHeavyweight', 'Middleweight', 'Welterweight',
      'Lightweight', 'Featherweight', 'Bantamweight', 'Flyweight',
      "WomensBantamweight", "WomensFlyweight", "WomensStrawweight"
    ].map(w => w.toLowerCase());

    // Загрузка стран, потом бойцов
    $http.get('/api/countries').then(function(response) {
        $scope.countryList = response.data;
        return $http.get('/api/fighters');
      }).then(function(response) {
        $scope.fighters = response.data;
        $scope.fighters.forEach(f => {
          if (typeof f.country === 'string') {
            const c = $scope.countryList.find(x => x.code === f.country);
            f.country = c ? angular.copy(c) : { code: f.country, name: f.country };
          }
        });

        const allClasses = new Set($scope.fighters.map(f => f.weightClass));
        $scope.weightClasses = Array.from(allClasses)
          .filter(w => !!w && w.toLowerCase() !== 'na')
          .sort((a, b) => {
            const ia = weightOrder.indexOf(a.toLowerCase());
            const ib = weightOrder.indexOf(b.toLowerCase());
            return (ia === -1 ? 999 : ia) - (ib === -1 ? 999 : ib);
          });

        regroup();
      })
      .catch(function(error) {
        console.error("Error loading data:", error);
      });

    function regroup() {
      let arr = $scope.fighters.slice();
      if ($scope.onlyActive === 'true')   arr = arr.filter(f => f.status === 'Active');
      if ($scope.onlyActive === 'false')  arr = arr.filter(f => f.status === 'Released');
      if ($scope.selectedWeightClass !== 'All') {
        arr = arr.filter(f => f.weightClass === $scope.selectedWeightClass);
      }

      const groupedMap = {};
      arr.forEach(f => {
        const key = f.weightClass;
        if (!key || key.toLowerCase() === 'na') return;
        if (!groupedMap[key]) groupedMap[key] = [];
        groupedMap[key].push(f);
      });

      $scope.filtered = Object.entries(groupedMap)
        .sort(([a], [b]) => {
          const idxA = weightOrder.indexOf(a.toLowerCase());
          const idxB = weightOrder.indexOf(b.toLowerCase());
          return (idxA === -1 ? 999 : idxA) - (idxB === -1 ? 999 : idxB);
        })
        .map(([cls, list]) => ({ class: cls, list }));
    }

    $scope.saveFighter = () => {
      if (typeof $scope.fighter.country === 'string') {
        const c = $scope.countryList.find(x => x.code === $scope.fighter.country);
        $scope.fighter.country = c ? angular.copy(c) : { code: $scope.fighter.country, name: $scope.fighter.country };
      }

      if ($scope.fighter.id) {
        $http.put('/api/fighters/' + $scope.fighter.id, $scope.fighter)
          .then(function(response) {
            const idx = $scope.fighters.findIndex(f => f.id === $scope.fighter.id);
            if (idx !== -1) {
              $scope.fighters[idx] = response.data;
              regroup();
              $scope.cancelEdit();
            }
          })
          .catch(function(error) {
            console.error("Error updating fighter:", error);
          });
      } else {
        $http.post('/api/fighters', $scope.fighter)
          .then(function(response) {
            $scope.fighters.push(response.data);
            regroup();
            $scope.cancelEdit();
          })
          .catch(function(error) {
            console.error("Error creating fighter:", error);
          });
      }
    };

    $scope.deleteFighter = function () {
      if (confirm('Delete ' + $scope.fighter.name + '?')) {
        $http.delete('/api/fighters/' + $scope.fighter.id)
          .then(function() {
            $scope.fighters = $scope.fighters.filter(f => f.id !== $scope.fighter.id);
            regroup();
            $scope.cancelEdit();
          })
          .catch(function(error) {
            console.error("Error deleting fighter:", error);
          });
      }
    };

    $scope.addFighter = () => {
      $scope.fighter = {status: 'Active'};
      $scope.mode = 'edit';
    };

    $scope.editFighter = f => {
      $scope.fighter = angular.copy(f);
      $scope.mode = 'edit';
    };

    $scope.cancelEdit = () => {
      $scope.fighter = {};
      $scope.mode = 'list';
    };

    $scope.$watchGroup(['selectedWeightClass', 'onlyActive', 'fighters'], regroup, { deep: true });

  });



//    $scope.countryList = [
//      { name: 'Afghanistan', code: 'AF' },
//      { name: 'Brazil', code: 'BR' },
//      { name: 'Canada', code: 'CA' },
//      { name: 'Ireland', code: 'IE' },
//      { name: 'Russia', code: 'RU' },
//      { name: 'Ukraine', code: 'UA' },
//      { name: 'United States', code: 'US' },
//      // ... добавь остальные по необходимости
//    ];
//    $scope.username  = 'test_manager';      // пустая строка, если не вошёл
//    $scope.userRole  = 'MANAGER';           // GUEST | MANAGER | ADMIN

  //    $scope.fighters = [
  //      {
  //        id: 1,
  //        name: 'Jon Jones',
  //        weightClass: 'Heavyweight',
  //        country: { name: 'United States', code: 'US' },
  //        ufcRecord: '20-1',
  //        mmaRecord: '27-1',
  //        status: 'Active'
  //      },
  //      {
  //        id: 2,
  //        name: 'Islam Makhachev',
  //        weightClass: 'Lightweight',
  //        country: { name: 'Russia', code: 'RU' },
  //        ufcRecord: '14-1',
  //        mmaRecord: '24-1',
  //        status: 'Active'
  //      },
  //      {
  //        id: 3,
  //        name: 'Charles Oliveira',
  //        weightClass: 'Lightweight',
  //        country: { name: 'Brazil', code: 'BR' },
  //        ufcRecord: '21-9',
  //        mmaRecord: '34-9',
  //        status: 'Released'
  //      }
  //    ];


