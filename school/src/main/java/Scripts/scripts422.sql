create TABLE drivers (
driverID BIGINT PRIMARY KEY,
name TEXT,
age INTEGER,
driverLicens BOOLEAN,
carId BIGINT REFERENCES cars (carId)
);

create TABLE cars (
carId BIGINT PRIMARY KEY,
carBrand TEXT,
carModel TEXT,
cost BIGINT
);