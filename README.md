# WhatsApp API Documentation

## Swagger Documentation
Explore and test the API endpoints using Swagger: [WhatsApp Swagger UI](https://whatsapp-latest.onrender.com/swagger-ui/index.html)

**Note:** Please be aware that the Swagger API may experience a delay in response when accessed for the first time or after a period of inactivity. This delay is due to the server being shut down by the hosting service (Render) when there is no activity. Kindly wait for a moment for the server to restart and serve the Swagger UI.

## Base URL
All endpoints are relative to the base URL: `https://whatsapp-latest.onrender.com`

## User Controller

### Sign In
- **Method:** POST
- **Endpoint:** `/user/signin`
- **Description:** Sign in with user credentials.
- **Request Body:**
  - User credentials.
- **Response:** Authentication token.

### Send OTP
- **Method:** POST
- **Endpoint:** `/user/send-otp`
- **Description:** Send OTP (One-Time Password) for authentication.
- **Request Body:**
  - User phone number.
- **Response:** OTP sent confirmation.

### Edit User Profile
- **Method:** POST
- **Endpoint:** `/user/edit/{id}`
- **Description:** Edit user profile information.
- **Parameters:**
  - `{id}`: The ID of the user.
- **Request Body:**
  - Updated user profile information.
- **Response:** HTTP status indicating success or failure.

## Conversation Controller

### Create Conversation
- **Method:** POST
- **Endpoint:** `/conversation/create`
- **Description:** Create a new conversation.
- **Request Body:**
  - Conversation details.
- **Response:** HTTP status indicating success or failure.

### Get Single Conversation
- **Method:** GET
- **Endpoint:** `/conversation/single/{id}`
- **Description:** Get details of a single conversation by ID.
- **Parameters:**
  - `{id}`: The ID of the conversation.
- **Response:** Conversation details.

### Get Conversation by Phone Number
- **Method:** GET
- **Endpoint:** `/conversation/byPhone/{phone}`
- **Description:** Get conversation details by phone number.
- **Parameters:**
  - `{phone}`: The phone number associated with the conversation.
- **Response:** Conversation details.

## Message Controller

### React to Message
- **Method:** PUT
- **Endpoint:** `/message/react/{id}`
- **Description:** React to a message with an emoji.
- **Parameters:**
  - `{id}`: The ID of the message to react to.
- **Response:** HTTP status indicating success or failure.

### Edit Message
- **Method:** PUT
- **Endpoint:** `/message/edit/{id}`
- **Description:** Edit a message.
- **Parameters:**
  - `{id}`: The ID of the message to edit.
- **Request Body:**
  - Message content.
- **Response:** HTTP status indicating success or failure.

### Open Message
- **Method:** POST
- **Endpoint:** `/message/open/{id}`
- **Description:** Mark a message as opened.
- **Parameters:**
  - `{id}`: The ID of the message to mark as opened.
- **Response:** HTTP status indicating success or failure.

### Create Message
- **Method:** POST
- **Endpoint:** `/message/create`
- **Description:** Create a new message.
- **Request Body:**
  - Message content.
- **Response:** HTTP status indicating success or failure.

### Get Messages by Conversation
- **Method:** GET
- **Endpoint:** `/message/{conversationId}`
- **Description:** Get messages by conversation ID with pagination.
- **Parameters:**
  - `{conversationId}`: The ID of the conversation.
- **Response:** List of messages.

### Delete Message
- **Method:** DELETE
- **Endpoint:** `/message/delete/{forEveryone}`
- **Description:** Delete a message for everyone in the conversation.
- **Parameters:**
  - `{forEveryone}`: Boolean indicating whether to delete the message for everyone.
- **Response:** HTTP status indicating success or failure.
