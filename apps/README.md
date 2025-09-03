# Mehanayim Choir Android Application

A modern Android application for the Mehanayim Choir team with User and Admin roles, featuring secure authentication, music streaming, social interactions, and comprehensive admin management tools.

## Features

### User Features
- **Secure Authentication**: Role-based login system (User/Admin)
- **Home Page**: Displays choir team information and pictures
- **Music Library**: Browse and play music with lyrics overlay
- **Social Feed**: Share posts, images, and interact with comments/replies
- **News**: Read choir news and announcements
- **Profile Management**: Edit personal information and upload images

### Admin Features
- **UI Editor**: No-code interface editor for customizing app appearance
- **Content Upload**: Upload music files, lyrics, and organize by categories
- **User Management**: Full control over user accounts (edit, block, delete)
- **News Publishing**: Create and publish news with rich content support

### Technical Features
- **Modern UI/UX**: Clean, responsive design with Material 3
- **Music Player**: Background playback with blurred image backgrounds
- **Real-time Updates**: Firebase integration for live data synchronization
- **Offline Support**: Local database caching with Room
- **Security**: Secure authentication and role-based access control

## Architecture

The application follows modern Android development best practices:

- **MVVM Architecture**: ViewModels with StateFlow for reactive UI
- **Dependency Injection**: Hilt for clean dependency management
- **Repository Pattern**: Centralized data access layer
- **Jetpack Compose**: Modern declarative UI framework
- **Room Database**: Local data persistence
- **Firebase**: Backend services (Auth, Firestore, Storage)

## Project Structure

```
app/
├── src/main/java/com/mehanayim/choir/
│   ├── data/
│   │   ├── model/          # Data models
│   │   ├── database/       # Room database setup
│   │   ├── dao/           # Data access objects
│   │   └── repository/    # Repository implementations
│   ├── ui/
│   │   ├── auth/          # Authentication screens
│   │   ├── user/          # User dashboard and pages
│   │   ├── admin/         # Admin dashboard and tools
│   │   ├── music/         # Music player and library
│   │   └── theme/         # App theming
│   ├── viewmodel/         # ViewModels for UI logic
│   ├── service/           # Background services
│   └── di/               # Dependency injection modules
└── src/main/res/         # Resources (layouts, strings, etc.)
```

## Setup Instructions

### Prerequisites
- Android Studio Arctic Fox or later
- Android SDK 24+ (Android 7.0)
- Kotlin 1.9.10+
- Firebase project setup

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/your-username/mehanayim-choir.git
   cd mehanayim-choir
   ```

2. **Firebase Setup**
   - Create a new Firebase project at [Firebase Console](https://console.firebase.google.com/)
   - Enable Authentication (Email/Password)
   - Enable Firestore Database
   - Enable Firebase Storage
   - Download `google-services.json` and place it in the `app/` directory
   - Update the `google-services.json` with your actual Firebase configuration

3. **Build and Run**
   ```bash
   ./gradlew assembleDebug
   ```

### Firebase Configuration

1. **Authentication**
   - Go to Firebase Console > Authentication > Sign-in method
   - Enable Email/Password authentication
   - Configure authorized domains

2. **Firestore Database**
   - Create a Firestore database in production mode
   - Set up security rules for user data access

3. **Storage**
   - Enable Firebase Storage
   - Configure storage rules for file uploads

## Usage

### User Flow
1. **Registration/Login**: Users create accounts or sign in
2. **Dashboard**: Access to Home, Music, Social, News, and Profile
3. **Music**: Browse and play music with lyrics
4. **Social**: Share content and interact with community
5. **News**: Read choir announcements and updates

### Admin Flow
1. **Admin Login**: Admins sign in with elevated privileges
2. **UI Editor**: Customize app appearance and content
3. **Upload Management**: Add music, lyrics, and organize content
4. **User Management**: Monitor and manage user accounts
5. **News Publishing**: Create and publish choir news

## Key Components

### Authentication System
- Firebase Authentication with email/password
- Role-based access control (User/Admin)
- Secure session management
- Password reset functionality

### Music System
- Audio file streaming and playback
- Lyrics display with synchronized timing
- Background music service
- Music categorization and organization

### Social Features
- Post creation with images and text
- Comment and reply system
- Like/unlike functionality
- Real-time updates

### Admin Tools
- No-code UI editor for interface customization
- Bulk content upload capabilities
- User account management
- News publishing with rich content

## Database Schema

### Users
- User profile information
- Role assignment (User/Admin)
- Account status and permissions

### Music
- Audio files and metadata
- Lyrics and categorization
- Upload tracking and organization

### Social Posts
- User-generated content
- Comments and replies
- Like tracking and engagement

### News
- Admin-published content
- Rich media support
- Publishing status and scheduling

## Security Features

- **Authentication**: Secure Firebase Auth integration
- **Authorization**: Role-based access control
- **Data Validation**: Input sanitization and validation
- **Secure Storage**: Encrypted local data storage
- **Network Security**: HTTPS communication with Firebase

## Performance Optimizations

- **Image Loading**: Coil for efficient image loading and caching
- **Database**: Room with optimized queries and indexing
- **UI**: Jetpack Compose with efficient recomposition
- **Background Tasks**: Coroutines for async operations
- **Memory Management**: Proper lifecycle management

## Testing

The application includes comprehensive testing:

- **Unit Tests**: ViewModels and business logic
- **Integration Tests**: Repository and database operations
- **UI Tests**: Compose UI testing
- **Firebase Tests**: Authentication and data operations

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Support

For support and questions:
- Create an issue in the GitHub repository
- Contact the development team
- Check the documentation wiki

## Roadmap

### Upcoming Features
- [ ] Push notifications for news and updates
- [ ] Advanced music playlist management
- [ ] Video content support
- [ ] Advanced admin analytics
- [ ] Multi-language support
- [ ] Dark theme customization
- [ ] Offline music download
- [ ] Advanced social features (mentions, hashtags)

### Version History
- **v1.0.0**: Initial release with core features
- **v1.1.0**: Enhanced UI editor and social features
- **v1.2.0**: Performance optimizations and bug fixes

## Acknowledgments

- Mehanayim Choir team for requirements and feedback
- Android development community for best practices
- Firebase team for excellent backend services
- Jetpack Compose team for modern UI framework
