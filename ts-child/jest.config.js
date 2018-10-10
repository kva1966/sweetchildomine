module.exports = {
    globals: {
        'ts-jest': {
            tsConfig: 'tsconfig.json'
        }
    },
    collectCoverageFrom: [
        "**/*.{js,ts}",
        "!**/jest.config.js",
        "!**/node_modules/**",
        "!**/dist/**",
        "!**/coverage/**"
    ],
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