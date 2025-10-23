# BBL Web App

A modern React/Next.js web application for the BBL (Bangkok Bank Learning) project.

## Overview

This is the frontend web application built with Next.js 16, React 19, and TypeScript. It provides the user interface for the BBL system and communicates with the backend API.

## Technology Stack

- **React**: 19.2.0
- **Next.js**: 16.0.0
- **TypeScript**: ^5
- **Tailwind CSS**: ^4
- **Node.js**: 20+

## Project Structure

```
bbl-web-app/
├── app/
│   ├── globals.css
│   ├── layout.tsx
│   └── page.tsx
├── public/
├── package.json
├── tsconfig.json
├── next.config.ts
├── eslint.config.mjs
├── postcss.config.mjs
└── next-env.d.ts
```

## Prerequisites

- Node.js 20 or higher
- npm, yarn, pnpm, or bun

## Getting Started

### Clone the Repository

```bash
git clone <repository-url>
cd bbl-test/bbl-web-app
```

### Install Dependencies

```bash
npm install
# or
yarn install
# or
pnpm install
# or
bun install
```

### Run the Development Server

```bash
npm run dev
# or
yarn dev
# or
pnpm dev
# or
bun dev
```

Open [http://localhost:3000](http://localhost:3000) with your browser to see the result.

You can start editing the page by modifying `app/page.tsx`. The page auto-updates as you edit the file.

## Available Scripts

- `npm run dev` - Starts the development server
- `npm run build` - Builds the application for production
- `npm run start` - Starts the production server
- `npm run lint` - Runs ESLint for code quality checks

## Building for Production

### Build the Application

```bash
npm run build
```

### Start Production Server

```bash
npm run start
```

The application will be available at `http://localhost:3000`.

## Features

- **App Router**: Uses Next.js 13+ App Router for modern routing
- **TypeScript**: Full TypeScript support for type safety
- **Tailwind CSS**: Utility-first CSS framework for styling
- **ESLint**: Code quality and consistency
- **Font Optimization**: Automatic font optimization with next/font

## Configuration

### Environment Variables

Create a `.env.local` file in the root directory for local environment variables:

```env
# API Configuration
NEXT_PUBLIC_API_URL=http://localhost:8080
```

### Tailwind CSS

Tailwind CSS is configured via `postcss.config.mjs`. Customize your design system in the configuration files.

## Development Guidelines

### Code Style

- Follow TypeScript best practices
- Use ESLint configuration for code consistency
- Prefer functional components with hooks
- Use Tailwind CSS for styling

### File Naming

- Use PascalCase for React components
- Use kebab-case for pages and API routes
- Use camelCase for utility functions

## API Integration

The frontend communicates with the BBL Backend API. Ensure the backend server is running on the configured URL (default: `http://localhost:8080`).

## Deployment

### Vercel (Recommended)

The easiest way to deploy your Next.js app is to use the [Vercel Platform](https://vercel.com/new?utm_medium=default-template&filter=next.js&utm_source=create-next-app&utm_campaign=create-next-app-readme).

1. Push your code to GitHub
2. Import your repository in Vercel
3. Deploy with zero configuration

### Other Platforms

This Next.js application can be deployed to any platform that supports Node.js:

- Netlify
- AWS Amplify
- DigitalOcean App Platform
- Railway
- Render

## Learn More

To learn more about the technologies used:

- [Next.js Documentation](https://nextjs.org/docs) - learn about Next.js features and API
- [React Documentation](https://react.dev) - learn about React
- [TypeScript Documentation](https://www.typescriptlang.org/docs/) - learn about TypeScript
- [Tailwind CSS Documentation](https://tailwindcss.com/docs) - learn about Tailwind CSS

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Run linting: `npm run lint`
5. Test your changes
6. Submit a pull request

## License

This project is part of a coding test for Bangkok Bank Learning (BBL).

## Support

For questions or issues, please contact the development team.
