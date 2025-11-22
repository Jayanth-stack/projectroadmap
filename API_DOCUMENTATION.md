# API Documentation

## Base URL
```
http://localhost:8080/api/urls
```

## Response Codes

| Code | Description |
|------|-------------|
| 200 | Successful GET or PUT request |
| 201 | Successful POST request (resource created) |
| 204 | Successful DELETE request |
| 400 | Bad Request (e.g., duplicate shortcode) |
| 404 | Not Found (shortcode doesn't exist) |
| 500 | Internal Server Error |

## Data Models

### CreateUrl (Request DTO)
```json
{
  "originalURL": "string (required)",
  "shortcode": "string (optional, 6 chars if not provided)"
}
```

### UrlResponse (Response DTO)
```json
{
  "shortcode": "string",
  "originalURL": "string",
  "createdAt": "ISO 8601 timestamp",
  "lastAccessedAt": "ISO 8601 timestamp (null if never accessed)",
  "accessCount": "integer (number of times accessed)"
}
```

### UpdateUrlRequest (Request DTO)
```json
{
  "originalURL": "string (required)"
}
```

## Detailed Endpoint Documentation

### Create Shortened URL

Creates a new shortened URL entry.

**Request:**
```
POST /api/urls
Content-Type: application/json

{
  "originalURL": "https://www.example.com/very-long-path/with/many/segments",
  "shortcode": "mycode"  // optional
}
```

**Response (201):**
```json
{
  "shortcode": "mycode",
  "originalURL": "https://www.example.com/very-long-path/with/many/segments",
  "createdAt": "2025-11-22T18:30:00Z",
  "lastAccessedAt": null,
  "accessCount": 0
}
```

**Error Response (400):**
```
When shortcode already exists
```

### Get URL by Shortcode

Retrieves URL information by shortcode. Automatically increments access count.

**Request:**
```
GET /api/urls/mycode
```

**Response (200):**
```json
{
  "shortcode": "mycode",
  "originalURL": "https://www.example.com/very-long-path/with/many/segments",
  "createdAt": "2025-11-22T18:30:00Z",
  "lastAccessedAt": "2025-11-22T18:35:15Z",
  "accessCount": 5
}
```

**Response (404):**
When shortcode doesn't exist.

### Update URL

Updates the original URL for an existing shortcode.

**Request:**
```
PUT /api/urls/mycode
Content-Type: application/json

{
  "originalURL": "https://www.newdomain.com/new-page"
}
```

**Response (200):**
```json
{
  "shortcode": "mycode",
  "originalURL": "https://www.newdomain.com/new-page",
  "createdAt": "2025-11-22T18:30:00Z",
  "lastAccessedAt": "2025-11-22T18:35:15Z",
  "accessCount": 5
}
```

**Response (404):**
When shortcode doesn't exist.

### Delete URL

Deletes a shortened URL entry.

**Request:**
```
DELETE /api/urls/mycode
```

**Response (204):**
No content - deletion successful.

**Response (404):**
When shortcode doesn't exist.

## Examples

### JavaScript/Node.js
```javascript
// Create shortened URL
const response = await fetch('http://localhost:8080/api/urls', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({
    originalURL: 'https://www.example.com/long-url',
    shortcode: 'abc123'
  })
});
const data = await response.json();

// Retrieve URL
const urlData = await fetch('http://localhost:8080/api/urls/abc123').then(r => r.json());

// Update URL
await fetch('http://localhost:8080/api/urls/abc123', {
  method: 'PUT',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({ originalURL: 'https://www.example.com/new-url' })
});

// Delete URL
await fetch('http://localhost:8080/api/urls/abc123', { method: 'DELETE' });
```

### Python
```python
import requests

BASE_URL = 'http://localhost:8080/api/urls'

# Create shortened URL
response = requests.post(BASE_URL, json={
    'originalURL': 'https://www.example.com/long-url',
    'shortcode': 'abc123'
})
print(response.json())

# Retrieve URL
response = requests.get(f'{BASE_URL}/abc123')
print(response.json())

# Update URL
response = requests.put(f'{BASE_URL}/abc123', json={
    'originalURL': 'https://www.example.com/new-url'
})
print(response.json())

# Delete URL
response = requests.delete(f'{BASE_URL}/abc123')
print(response.status_code)  # 204
```

### Java with HttpClient
```java
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

HttpClient client = HttpClient.newHttpClient();

// Create shortened URL
HttpRequest request = HttpRequest.newBuilder()
    .uri(URI.create("http://localhost:8080/api/urls"))
    .header("Content-Type", "application/json")
    .POST(HttpRequest.BodyPublishers.ofString(
        "{\"originalURL\":\"https://example.com\",\"shortcode\":\"abc123\"}"))
    .build();

HttpResponse<String> response = client.send(request, 
    HttpResponse.BodyHandlers.ofString());
System.out.println(response.body());
```

## Error Handling

The API returns appropriate HTTP status codes and error responses:

### 400 Bad Request
Returned when:
- Shortcode already exists
- Invalid request body

### 404 Not Found
Returned when:
- Shortcode doesn't exist in the database

### 500 Internal Server Error
Returned when:
- MongoDB connection fails
- Unexpected server error

## Performance Considerations

- Shortcodes are 6 characters long (alphanumeric)
- Auto-generated shortcodes are guaranteed to be unique
- MongoDB indexing on `shortcode` field (primary key) ensures fast lookups
- Access count and timestamp updates are performed on retrieval

## Rate Limiting

Currently no rate limiting is implemented. Consider adding it for production use.

## Security Notes

- No authentication/authorization implemented in this version
- MongoDB URI should be secured with credentials in production
- HTTPS should be used in production
- Input validation is basic - consider adding stricter validation for URLs
