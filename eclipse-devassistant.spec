%global install_loc    %{_datadir}/eclipse/dropins/packager

Name:           eclipse-devassistant
Version:        0.1.0
Release:        1%{?dist}
Summary:        Devassistant for Eclipse

Group:          Development/Tools
License:        EPL
URL:            https://fedorahosted.org/eclipse-devassistant
Source0:        https://fedorahosted.org/released/eclipse-devassistant/%{name}-%{version}.tar.gz


BuildArch: noarch

BuildRequires: java-devel
# We really need Indigo for 0.2 and up, due to a .spec file editor
# problem, but FP for Eclipse itself doesn't really have any Indigo
# deps.
BuildRequires: eclipse-pde >= 1:3.7.0
# Since 1.1 egit has the remote tracking fix
BuildRequires: eclipse-egit >= 1.1.0
Requires: eclipse-egit >= 1.1.0
Requires: devassistant
Requires: snakeyaml

%description
Devassistant for Eclipse is an Eclipse plug-in, which helps
Fedora contributors to interact with devassistant project

%prep
%setup -q -n eclipse-devassistant-%{version}

%build

%install
install -d -m 755 %{buildroot}%{install_loc}
mkdir -p %{buildroot}%{install_loc}/eclipse
mv org.fedoraproject.eclipse.packager-p2-repo/target/repository/features %{buildroot}%{install_loc}/eclipse/features
mv org.fedoraproject.eclipse.packager-p2-repo/target/repository/plugins %{buildroot}%{install_loc}/eclipse/plugins

%files
%{install_loc}
%doc org.fedoraproject.developer.assistant.feature/*.html

%changelog
* Thu Jun 20 2013 Petr Hracek <phracek@redhat.com> - 0.1.0-1
- Initial version
