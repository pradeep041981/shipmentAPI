// Shipment Tracking Angular Application
var app = angular.module('shipmentApp', []);

app.controller('ShipmentController', ['$scope', '$http', function($scope, $http) {
    // Initialize form data
    $scope.formData = {
        shipmentId: '',
        shipmentType: '',
        attributes: {
            attr1: false,
            attr2: false,
            attr3: false
        },
        comments: ''
    };

    $scope.shipmentList = [];
    $scope.submitted = false;
    $scope.successMessage = false;
    $scope.errorMessage = '';
    $scope.loading = false;

    // Submit form
    $scope.submitForm = function() {
        $scope.submitted = true;
        $scope.successMessage = false;
        $scope.errorMessage = '';

        // Validate required fields
        if (!$scope.formData.shipmentId || !$scope.formData.shipmentType) {
            return;
        }

        // Prepare attributes array
        var attributesArray = [];
        if ($scope.formData.attributes.attr1) attributesArray.push('attr1');
        if ($scope.formData.attributes.attr2) attributesArray.push('attr2');
        if ($scope.formData.attributes.attr3) attributesArray.push('attr3');

        // Prepare request payload
        var requestData = {
            shipmentId: $scope.formData.shipmentId,
            shipmentType: $scope.formData.shipmentType,
            attributes: attributesArray,
            comments: $scope.formData.comments
        };

        $scope.loading = true;

        // Send POST request to API
        $http.post('/api/shipment', requestData)
            .then(function(response) {
                $scope.loading = false;
                $scope.shipmentList = response.data;
                $scope.successMessage = true;

                // Hide success message after 3 seconds
                setTimeout(function() {
                    $scope.$apply(function() {
                        $scope.successMessage = false;
                    });
                }, 3000);

                // Reset form
                resetForm();
            })
            .catch(function(error) {
                $scope.loading = false;
                $scope.errorMessage = error.data?.message || 'Failed to submit form. Please try again.';
                console.error('Error:', error);
            });
    };

    // Reset form
    function resetForm() {
        $scope.formData = {
            shipmentId: '',
            shipmentType: '',
            attributes: {
                attr1: false,
                attr2: false,
                attr3: false
            },
            comments: ''
        };
        $scope.submitted = false;
    }
}]);

