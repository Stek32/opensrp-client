function FPSmartRegistryBridge() {
    var fpSmartRegistryContext = window.context;
    if (typeof fpSmartRegistryContext === "undefined" && typeof FakeFPSmartRegistryContext !== "undefined") {
        fpSmartRegistryContext = new FakeFPSmartRegistryContext();
    }

    return {
        getFPClients: function () {
            return JSON.parse(fpSmartRegistryContext.get());
        }
    };
}

function FakeFPSmartRegistryContext() {
    return {
        get: function () {
            return JSON.stringify([
                {
                    "ec_number": "2",
                    "fp_method": "female_sterilization",
                    "husband_name": "Manikyam",
                    "village": "Bherya",
                    "name": "Ammulu",
                    "thayi": "",
                    "isHighPriority": false,
                    "side_effects": "poops a lot",
                    "days_due": "2013/01/01",
                    "due_message": "due message",
                    "age": '24',
                    "num_pregnancies": '3',
                    "parity": '2',
                    "num_living_children": '1',
                    "num_stillbirths": '1',
                    "num_abortions": '1'

                },
                {
                    "ec_number": "2",
                    "fp_method": "female_sterilization",
                    "husband_name": "Manikyam",
                    "village": "Bherya",
                    "name": "Ammulu",
                    "thayi": "",
                    "isHighPriority": false,
                    "side_effects": "poops a bit",
                    "days_due": "2013/01/01",
                    "due_message": "due message",
                    "age": '20',
                    "num_pregnancies": '0',
                    "parity": '2',
                    "num_living_children": '1',
                    "num_stillbirths": '1',
                    "num_abortions": '1'
                },
                {
                    "ec_number": "9",
                    "fp_method": "iud",
                    "husband_name": "Umesh",
                    "village": "Bherya",
                    "name": "Amrutha",
                    "thayi": "369258",
                    "isHighPriority": false,
                    "side_effects": "poops a ton",
                    "days_due": "2013/01/01",
                    "due_message": "due message 112",
                    "age": '26',
                    "num_pregnancies": '1',
                    "parity": '2',
                    "num_living_children": '1',
                    "num_stillbirths": '2',
                    "num_abortions": '0'
                },
                {
                    "ec_number": "1",
                    "fp_method": "condom",
                    "husband_name": "Anji",
                    "village": "Chikkabherya",
                    "name": "Anitha",
                    "thayi": "2539641",
                    "isHighPriority": true,
                    "side_effects": "poops a lot",
                    "days_due": "2013/01/01",
                    "due_message": "due message",
                    "age": '24',
                    "num_pregnancies": '3',
                    "parity": '2',
                    "num_living_children": '1',
                    "num_stillbirths": '1',
                    "num_abortions": '1'
                },
                {
                    "ec_number": "7",
                    "fp_method": "male_sterilization",
                    "husband_name": "Hemanth",
                    "village": "somanahalli_colony",
                    "name": "Anu",
                    "thayi": "",
                    "isHighPriority": false,
                    "side_effects": "poops a lot",
                    "days_due": "2013/01/01",
                    "due_message": "due message",
                    "age": '24',
                    "num_pregnancies": '3',
                    "parity": '2',
                    "num_living_children": '1',
                    "num_stillbirths": '1',
                    "num_abortions": '1'
                },
                {
                    "ec_number": "10",
                    "fp_method": "female_sterilization",
                    "husband_name": "Nandisha",
                    "village": "Bherya",
                    "name": "Bibi",
                    "thayi": "",
                    "isHighPriority": false,
                    "side_effects": "poops a lot",
                    "days_due": "2013/01/01",
                    "due_message": "due message",
                    "age": '24',
                    "num_pregnancies": '3',
                    "parity": '2',
                    "num_living_children": '1',
                    "num_stillbirths": '1',
                    "num_abortions": '1'
                },
                {
                    "ec_number": "3",
                    "fp_method": "none",
                    "husband_name": "Biju Nayak",
                    "village": "Bherya",
                    "name": "Bindu",
                    "thayi": "1234567",
                    "isHighPriority": false,
                    "side_effects": "poops a lot",
                    "days_due": "2013/01/01",
                    "due_message": "due message",
                    "age": '24',
                    "num_pregnancies": '3',
                    "parity": '2',
                    "num_living_children": '1',
                    "num_stillbirths": '1',
                    "num_abortions": '1'
                },
                {
                    "ec_number": "4",
                    "fp_method": "none",
                    "husband_name": "Naresh",
                    "village": "Bherya",
                    "name": "Devi",
                    "thayi": "235689",
                    "isHighPriority": false,
                    "side_effects": "poops a lot",
                    "days_due": "2013/01/01",
                    "due_message": "due message",
                    "age": '24',
                    "num_pregnancies": '3',
                    "parity": '2',
                    "num_living_children": '1',
                    "num_stillbirths": '1',
                    "num_abortions": '1'
                },
                {
                    "ec_number": "11",
                    "fp_method": "none",
                    "husband_name": "Suresh",
                    "village": "Bherya",
                    "name": "Kavitha",
                    "thayi": "123456",
                    "isHighPriority": false,
                    "side_effects": "poops a lot",
                    "days_due": "2013/01/01",
                    "due_message": "due message",
                    "age": '24',
                    "num_pregnancies": '3',
                    "parity": '2',
                    "num_living_children": '1',
                    "num_stillbirths": '1',
                    "num_abortions": '1'
                },
                {
                    "ec_number": "13",
                    "fp_method": "female_sterilization",
                    "husband_name": "Kalyan",
                    "village": "Bherya",
                    "name": "Lakshmi",
                    "thayi": "12369",
                    "isHighPriority": false,
                    "side_effects": "poops a lot",
                    "days_due": "2013/01/01",
                    "due_message": "due message",
                    "age": '24',
                    "num_pregnancies": '3',
                    "parity": '2',
                    "num_living_children": '1',
                    "num_stillbirths": '1',
                    "num_abortions": '1'
                },
                {
                    "ec_number": "13",
                    "fp_method": "none",
                    "husband_name": "vinod",
                    "village": "Bherya",
                    "name": "Latha",
                    "thayi": "147285",
                    "isHighPriority": false,
                    "side_effects": "poops a lot",
                    "days_due": "2013/01/01",
                    "due_message": "due message",
                    "age": '24',
                    "num_pregnancies": '3',
                    "parity": '2',
                    "num_living_children": '1',
                    "num_stillbirths": '1',
                    "num_abortions": '1'
                },
                {
                    "ec_number": "8",
                    "fp_method": "female_sterilization",
                    "husband_name": "Raja",
                    "village": "somanahalli_colony",
                    "name": "Mahithi",
                    "thayi": "",
                    "isHighPriority": false,
                    "side_effects": "poops a lot",
                    "days_due": "2013/01/01",
                    "due_message": "due message",
                    "age": '24',
                    "num_pregnancies": '3',
                    "parity": '2',
                    "num_living_children": '1',
                    "num_stillbirths": '1',
                    "num_abortions": '1'
                },
                {
                    "ec_number": "12",
                    "fp_method": "none",
                    "husband_name": "Naresh",
                    "village": "Chikkabherya",
                    "name": "Raji",
                    "thayi": "258399",
                    "isHighPriority": false,
                    "side_effects": "poops a lot",
                    "days_due": "2013/01/01",
                    "due_message": "due message",
                    "age": '24',
                    "num_pregnancies": '3',
                    "parity": '2',
                    "num_living_children": '1',
                    "num_stillbirths": '1',
                    "num_abortions": '1'
                },
                {
                    "ec_number": "1",
                    "fp_method": "condom",
                    "husband_name": "Raja",
                    "village": "bherya",
                    "name": "Rani",
                    "thayi": "48666",
                    "isHighPriority": false,
                    "side_effects": "poops a lot",
                    "days_due": "2013/01/01",
                    "due_message": "due message",
                    "age": '24',
                    "num_pregnancies": '3',
                    "parity": '2',
                    "num_living_children": '1',
                    "num_stillbirths": '1',
                    "num_abortions": '1'
                }
            ]);
        }
    };
}