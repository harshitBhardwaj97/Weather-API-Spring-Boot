# Weather API

This project provides an API for retrieving weather data using an external weather service.

The API allows users to get today's weather for a given location, as well as weather data for a specific date range.

It uses third party [Weather API](https://www.visualcrossing.com/weather-api),
and is based on [Weather API Project](https://roadmap.sh/projects/weather-api-wrapper-service),
by [Roadmap.sh](https://roadmap.sh/).

It also includes automated integration tests for the weather API controllers,
ensuring that the API endpoints function as expected.

## API Endpoints

### 1. Get Today's Weather

**Endpoint:** `GET /api/weather/{location}`

- **Description**: Retrieves today's weather for the given location.
- **Parameters**:
    - `location` (required): The location for which the weather is to be retrieved (
      e.g., `Delhi`, `New York`, `London`).
- **Response**:
    - A JSON object containing weather data for the current day.

#### Example Request:

```
GET http//:localhost:8080/api/weather/delhi
```

#### Example Response:

```json
{
  "latitude": 28.6341,
  "longitude": 77.2169,
  "resolvedAddress": "Delhi, DL, India",
  "timezone": "Asia/Kolkata",
  "description": "Similar temperatures continuing with no rain expected.",
  "days": [
    {
      "datetime": "2024-11-19",
      "tempmax": 26.5,
      "tempmin": 14,
      "humidity": 58,
      "conditions": "Partially cloudy",
      "description": "Partly cloudy throughout the day."
    }
  ]
}
```

### 2. Get Weather Between Date Range

**Endpoint:** `GET /api/weather/{location}/range?startDate=&endDate=`

- **Description**: Retrieves today's weather for the given location.
- **Path Variable(s)**:
    - `location` (required): The location for which the weather is to be retrieved (
      e.g., `Delhi`, `New York`, `London`).
- **Query Parameters**:
    - `startDate` (required): The start date of the range in YYYY-MM-DD format.
    - `endDate` (required): The end date of the range in YYYY-MM-DD format.
- **Response**:
    - A JSON object containing weather data for the date range specified.

#### Example Request:

```
GET http//:localhost:8080/api/weather/delhi/range?startDate=2024-10-01&endDate=2024-10-03
```

#### Example Response:

```json
{
  "latitude": 28.6341,
  "longitude": 77.2169,
  "resolvedAddress": "Delhi, DL, India",
  "timezone": "Asia/Kolkata",
  "description": "No description available for range type response.",
  "days": [
    {
      "datetime": "2024-10-01",
      "tempmax": 34.3,
      "tempmin": 25,
      "humidity": 77.6,
      "conditions": "Clear",
      "description": "Clear conditions throughout the day."
    },
    {
      "datetime": "2024-10-02",
      "tempmax": 35.9,
      "tempmin": 26,
      "humidity": 67.4,
      "conditions": "Clear",
      "description": "Clear conditions throughout the day."
    },
    {
      "datetime": "2024-10-03",
      "tempmax": 35.6,
      "tempmin": 27,
      "humidity": 61.7,
      "conditions": "Clear",
      "description": "Clear conditions throughout the day."
    }
  ]
}
```

## Error Handling

The API provides meaningful error messages when something goes wrong. Below are common error scenarios and their
corresponding responses.

### Invalid Location

When an invalid location is provided, the API will return an error message like this:

```json
{
  "message": "400 : \"Bad API Request:Invalid location parameter value.\"",
  "successStatus": false,
  "httpStatus": "BAD_REQUEST"
}
```

### Invalid Date Format

If the date format is incorrect (not in YYYY-MM-DD format), you will get an error message like:

```json
{
  "message": "Pass a valid date in YYYY-MM-DD format.",
  "successStatus": false,
  "httpStatus": "BAD_REQUEST"
}
```

### Invalid Date Range

When the end date is before the start date, the API will return the following error message:

```json
{
  "message": "End date cannot come before start date.",
  "successStatus": false,
  "httpStatus": "BAD_REQUEST"
}
```

### Invalid API_KEY

In case of invalid API_KEY, the following error message will be generated:

```json
{
  "message": "Make sure you have configured a valid API_KEY.",
  "successStatus": false,
  "httpStatus": "UNAUTHORIZED"
}
```

### Invalid endpoint

If an invalid endpoint is requested, you will get the following error response:

```json
{
  "message": "No static resource <invalid-endpoint>.",
  "successStatus": false,
  "httpStatus": "BAD_REQUEST"
}
```

### Missing startDate or endDate

In case startDate or endDate are missing, then you will get the following kind of error response:

```json
{
  "message": "Required parameter 'startDate' is missing.",
  "successStatus": false,
  "httpStatus": "BAD_REQUEST"
}
```

### Redis Server Not Running

In case REDIS Server is not up and running, before making any request, you will get:

```json
{
  "message": "Make sure REDIS server is up and running, then again make the request.",
  "successStatus": false,
  "httpStatus": "BAD_REQUEST"
}
```

### Rate Limit is Exceeded

In case rate limit gets exceeded for any endpoint, you will get error response like:

```json
{
  "message": "RateLimiter 'todays-weather' does not permit further calls",
  "successStatus": false,
  "httpStatus": "TOO_MANY_REQUESTS"
}
```

## Give a Star 🌟

If you find this repository useful, **please give it a star!** ⭐️.

Thank you for your support! 🎉.