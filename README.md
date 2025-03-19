# EmailAgent
# EmailAgent - AI-powered Email and Calendar Assistant

EmailAgent is an intelligent email and calendar management application built with **Spring Boot, Spring AI, and Google APIs**.  
It integrates with **Gmail and Google Calendar** to automatically read, analyze, and respond to emails while managing events dynamically.

## Prerequisites

Before running the application, make sure you have the following installed:

### **Install Ollama Locally**
EmailAgent uses **Ollama** as the AI backend. You need to install it and download the necessary models.

#### **Install Ollama**
- **Mac (Homebrew)**
  ```bash
  brew install ollama

## Features

**AI-powered Email Processing**: Extracts appointment details from emails using **Ollama LLM**.  
**Google Calendar Integration**: Checks availability and schedules events automatically.
**Automated Email Replies**: Sends professional responses based on email content and calendar status.  
**Dynamic Tool Execution**: Uses **Spring AI** to decide which tools to invoke based on the user’s request.  
**Secure OAuth 2.0 Authentication**: Connects to Google services with user consent.

---

### **How It Works**
1. **User requests an action** (e.g., "Check my emails and schedule a meeting").
2. **Ollama AI analyzes the request** and dynamically decides which tool(s) to use.
3. **GmailTool reads emails** and extracts relevant information.
4. **CalendarTool checks availability** and schedules events if necessary.
5. **A professional response is sent** via Gmail with the AI-generated reply.
