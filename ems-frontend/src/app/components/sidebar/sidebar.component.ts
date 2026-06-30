import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { MatListModule } from '@angular/material/list';
import { MatIconModule } from '@angular/material/icon';
import { MatDividerModule } from '@angular/material/divider';
import { AuthService } from '../../services/auth.service';

interface NavItem {
  label: string;
  icon: string;
  route: string;
  adminOnly?: boolean;
}

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [CommonModule, RouterLink, RouterLinkActive, MatListModule, MatIconModule, MatDividerModule],
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.scss']
})
export class SidebarComponent {
  navItems: NavItem[] = [
  { label: 'Dashboard', icon: 'dashboard', route: '/dashboard' },
  { label: 'Employee List', icon: 'people', route: '/employees' },
  { label: 'Attendance', icon: 'fingerprint', route: '/attendance' },
  { label: 'Add Employee', icon: 'person_add', route: '/employees/add', adminOnly: true },
];

  constructor(public authService: AuthService) {}

  get visibleItems(): NavItem[] {
    return this.navItems.filter(item => !item.adminOnly || this.authService.isAdmin());
  }
}