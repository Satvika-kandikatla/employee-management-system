import { Injectable } from '@angular/core';

/**
 * THEME SERVICE
 * -------------
 * Manages dark/light mode toggle.
 * Saves preference to localStorage so it persists after page refresh.
 */
@Injectable({
  providedIn: 'root'
})
export class ThemeService {

  private readonly THEME_KEY = 'theme';
  private isDark = false;

  constructor() {
    // Load saved theme on startup
    const saved = localStorage.getItem(this.THEME_KEY);
    if (saved === 'dark') {
      this.enableDark();
    }
  }

  toggleTheme(): void {
    if (this.isDark) {
      this.enableLight();
    } else {
      this.enableDark();
    }
  }

  private enableDark(): void {
    document.documentElement.classList.add('dark-mode');
    localStorage.setItem(this.THEME_KEY, 'dark');
    this.isDark = true;
  }

  private enableLight(): void {
    document.documentElement.classList.remove('dark-mode');
    localStorage.setItem(this.THEME_KEY, 'light');
    this.isDark = false;
  }

  isDarkMode(): boolean {
    return this.isDark;
  }
}