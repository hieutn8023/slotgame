Here's the updated **README.md** file, including the benchmark API details and optional response examples.

```markdown
# Slot Game

## Build and Run
To build and run the project, use the following commands:

```sh
./gradlew clean build
./gradlew bootRun
```

## Getting Started
This section provides the available API endpoints.

### Fetch Game Configuration
- **Method:** `GET`
- **URL:** `http://localhost:8070/api/config`
- **Description:** Returns the game configuration details.

### Spin the Slot Machine
- **Method:** `POST`
- **URL:** `http://localhost:8070/api/spin`
- **Description:** Triggers a slot game spin and returns the spin result, winning lines, and total winnings.

#### **Example Response**
```json
{
  "spinResult": [
    [1, 2, "Wild", 4, 5],
    [5, "Wild", 3, 2, 1],
    [3, 3, 3, 7, 8]
  ],
  "winningLines": [
    [[2,0], [2,1], [2,2]],
    [[1,0], [1,1], [1,2]]
  ],
  "totalWin": 150
}
```

### Benchmark Multiple Spins
- **Method:** `POST`
- **URL:** `http://localhost:8070/api/benchmark?spins=50000`
- **Description:** Runs multiple spins to benchmark performance.

#### **Example Response**
```json
{
  "totalSpins": 50000,
  "averageWin": 47.5,
  "executionTimeMs": 1203
}
```