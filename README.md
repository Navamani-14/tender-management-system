Tender Management System

Description:
A backend application built using Java and Spring Boot to manage tender
bidding and approval workflows with secure, role-based access.

Features:
- Bidders can submit bids for tenders
- Approvers can review, approve, or reject bids
- Secure APIs using Spring Security
- Validation and duplicate bid prevention
- Role and ownership-based authorization

Security:
- Implemented Spring Security for authentication
- Logged-in user details accessed using @AuthenticationPrincipal
- Only bid owner or approver can delete a bid
- Unauthorized access is restricted

API Highlights:
- POST /bidding/add        -> Submit a new bid
- GET /bidding/list       -> List bids based on bid amount
- PATCH /bidding/update   -> Update bid status
- DELETE /bidding/delete  -> Delete bid (authorized users only)

Tech Stack:
- Java
- Spring Boot
- Spring Security
- Spring Data JPA
- REST APIs
- JPA
- Maven

Architecture:
Controller → Service → Repository → Database

Outcome:
This project demonstrates real-world backend development including
secure REST APIs, role-based authorization, and workflow-based business logic.

Navamani
Java Backend Developer
