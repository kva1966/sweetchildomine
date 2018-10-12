module.exports = {
    globals: {
        'ts-jest': {
            tsConfig: 'tsconfig.json'
        }
    },
    collectCoverageFrom: [
        "**/src/**/*.{js,ts}"
    ],
    moduleNameMapper: {
        '^@app/(.*)$': '<rootDir>/src/$1'
    },
    moduleFileExtensions: [
        'ts',
        'js'
    ],
    transform: {
        '^.+\\.(ts|tsx)$': 'ts-jest'
    },
    testMatch: [
        '**/test/**/*.test.(ts|js)'
    ],
    testEnvironment: 'node'
};
