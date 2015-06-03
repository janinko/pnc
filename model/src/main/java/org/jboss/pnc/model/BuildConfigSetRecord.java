/**
 * JBoss, Home of Professional Open Source.
 * Copyright 2014 Red Hat, Inc., and individual contributors
 * as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.pnc.model;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotNull;

/**
 * This class contains a summary of the build results of the execution of a build config set. This includes the start and end
 * time, links to the build records for the executed builds, and the overall status (success/failure) of the set execution.
 */
@Entity
public class BuildConfigSetRecord implements GenericEntity<Integer> {

    private static final long serialVersionUID = 8917142499701376822L;

    public static final String SEQUENCE_NAME = "build_config_set_record_id_seq";
    public static final String PREPARED_STATEMENT_INSERT = "INSERT INTO buildconfigsetrecord("
            + "id, endtime, starttime, status, buildconfigurationset_id, " + "productversion_id, user_id) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?);";

    @Id
    @SequenceGenerator(name = SEQUENCE_NAME, sequenceName = SEQUENCE_NAME, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_NAME)
    private Integer id;

    /**
     * The build configuration set which was executed
     */
    @NotNull
    @ManyToOne
    private BuildConfigurationSet buildConfigurationSet;

    /**
     * The time at which the first build in the set was started
     */
    @NotNull
    private Timestamp startTime;

    /**
     * The time at which the last build in the set was completed
     */
    @NotNull
    private Timestamp endTime;

    /**
     * The user who executed the set.
     */
    // @NotNull //TODO uncomment
    @ManyToOne
    private User user;

    /**
     * The status (success/failure) of the overall set. If any builds in the set failed, the status of the set is failed.
     */
    @Enumerated(value = EnumType.STRING)
    private BuildStatus status;

    /**
     * The detailed records of the builds that were executed as part of the execution of this set
     * 
     */
    @OneToMany(mappedBy = "buildConfigSetRecord")
    private Set<BuildRecord> buildRecords;

    @ManyToOne
    private ProductVersion productVersion;

    /**
     * Instantiates a new project build result.
     */
    public BuildConfigSetRecord() {
        buildRecords = new HashSet<BuildRecord>();
    }

    /**
     * Gets the id.
     *
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the id.
     *
     * @param id the new id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Gets the start time.
     *
     * @return the start time
     */
    public Timestamp getStartTime() {
        return startTime;
    }

    /**
     * Sets the start time.
     *
     * @param startTime the new start time
     */
    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    /**
     * Gets the end time.
     *
     * @return the end time
     */
    public Timestamp getEndTime() {
        return endTime;
    }

    /**
     * Sets the end time.
     *
     * @param endTime the new end time
     */
    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    /**
     * Gets the user.
     *
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets the user.
     *
     * @param user the new user
     */
    public void setUser(User user) {
        this.user = user;
    }

    public BuildConfigurationSet getBuildConfigurationSet() {
        return buildConfigurationSet;
    }

    public void setBuildConfigurationSet(BuildConfigurationSet buildConfigurationSet) {
        this.buildConfigurationSet = buildConfigurationSet;
    }

    /**
     * Gets the status.
     *
     * @return the status
     */
    public BuildStatus getStatus() {
        return status;
    }

    /**
     * Sets the status.
     *
     * @param status the new status
     */
    public void setStatus(BuildStatus status) {
        this.status = status;
    }

    public Set<BuildRecord> getBuildRecords() {
        return buildRecords;
    }

    public void setBuildRecords(Set<BuildRecord> buildRecords) {
        this.buildRecords = buildRecords;
    }

    public boolean addBuildRecord(BuildRecord buildRecord) {
        return buildRecords.add(buildRecord);
    }

    public boolean removeBuildRecord(BuildRecord buildRecord) {
        return buildRecords.remove(buildRecord);
    }

    public ProductVersion getProductVersion() {
        return productVersion;
    }

    /**
     * Sets the system image.
     *
     * @param systemImage the new system image
     */
    public void setProductVersion(ProductVersion productVersion) {
        this.productVersion = productVersion;
    }

    @Override
    public String toString() {
        return "BuildConfigSetRecord [id=" + id + ", buildConfigurationSet=" + buildConfigurationSet.getName() + "]";
    }

    public static class Builder {

        private Integer id;

        private BuildConfigurationSet buildConfigurationSet;

        private Timestamp startTime;

        private Timestamp endTime;

        private BuildStatus status;

        private User user;

        private ProductVersion productVersion;

        private Set<BuildRecord> buildRecords;

        public Builder() {
            buildRecords = new HashSet<BuildRecord>();
        }

        public static Builder newBuilder() {
            return new Builder();
        }

        public BuildConfigSetRecord build() {
            BuildConfigSetRecord buildConfigSetRecord = new BuildConfigSetRecord();
            buildConfigSetRecord.setId(id);
            buildConfigSetRecord.setBuildConfigurationSet(buildConfigurationSet);
            buildConfigSetRecord.setStartTime(startTime);
            buildConfigSetRecord.setEndTime(endTime);
            buildConfigSetRecord.setUser(user);
            buildConfigSetRecord.setProductVersion(productVersion);
            buildConfigSetRecord.setStatus(status);

            // Set the bi-directional mapping
            for (BuildRecord buildRecord : buildRecords) {
                buildRecord.setBuildConfigSetRecord(buildConfigSetRecord);
            }
            buildConfigSetRecord.setBuildRecords(buildRecords);

            return buildConfigSetRecord;
        }

        public Builder id(Integer id) {
            this.id = id;
            return this;
        }

        public Builder buildConfigurationSet(BuildConfigurationSet buildConfigurationSet) {
            this.buildConfigurationSet = buildConfigurationSet;
            return this;
        }

        public Builder startTime(Timestamp startTime) {
            this.startTime = startTime;
            return this;
        }

        public Builder endTime(Timestamp endTime) {
            this.endTime = endTime;
            return this;
        }

        public Builder user(User user) {
            this.user = user;
            return this;
        }

        public Builder status(BuildStatus status) {
            this.status = status;
            return this;
        }

        public Builder productVersion(ProductVersion productVersion) {
            this.productVersion = productVersion;
            return this;
        }

        public Builder buildRecords(Set<BuildRecord> buildRecords) {
            this.buildRecords = buildRecords;
            return this;
        }

    }

}
