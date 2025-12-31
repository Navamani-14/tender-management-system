
package com.example.tenderTest.config;

import com.example.tenderTest.model.RoleModel;
import com.example.tenderTest.model.UserModel;
import com.example.tenderTest.repository.RoleRepository;
import com.example.tenderTest.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

@Configuration
public class DataLoader implements CommandLineRunner {

    private final RoleRepository roleRepo;
    private final UserRepository userRepo;
    private final BCryptPasswordEncoder encoder;

    public DataLoader(RoleRepository roleRepo, UserRepository userRepo, BCryptPasswordEncoder encoder) {
        this.roleRepo = roleRepo;
        this.userRepo = userRepo;
        this.encoder = encoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // 1️⃣ Load roles if not already present
        if (roleRepo.count() == 0) {
            RoleModel bidder = new RoleModel();
            bidder.setRolename("BIDDER");
            roleRepo.save(bidder);

            RoleModel approver = new RoleModel();
            approver.setRolename("APPROVER");
            roleRepo.save(approver);
        }

        // 2️⃣ Load users if not already present
        if (userRepo.count() == 0) {
            Optional<RoleModel> bidderRoleOpt = roleRepo.findByRolename("BIDDER");
            Optional<RoleModel> approverRoleOpt = roleRepo.findByRolename("APPROVER");

            RoleModel bidderRole = bidderRoleOpt.orElseThrow(() -> new RuntimeException("BIDDER role not found"));
            RoleModel approverRole = approverRoleOpt.orElseThrow(() -> new RuntimeException("APPROVER role not found"));

            UserModel u1 = new UserModel();
            u1.setUsername("bidder1");
            u1.setCompanyName("companyOne");
            u1.setEmail("bidderemail@gmail.com");
            u1.setPassword(encoder.encode("bidder123$"));
            u1.setRole(bidderRole);
            userRepo.save(u1);

            UserModel u2 = new UserModel();
            u2.setUsername("bidder2");
            u2.setCompanyName("companyTwo");
            u2.setEmail("bidderemail2@gmail.com");
            u2.setPassword(encoder.encode("bidder789$"));
            u2.setRole(bidderRole);
            userRepo.save(u2);

            UserModel u3 = new UserModel();
            u3.setUsername("approver");
            u3.setCompanyName("defaultCompany");
            u3.setEmail("approveremail@gmail.com");
            u3.setPassword(encoder.encode("approver123$"));
            u3.setRole(approverRole);
            userRepo.save(u3);
        }
    }
}
