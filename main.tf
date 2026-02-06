# AWS 서비스 리전 서울로 설정
provider "aws" {
  region = "ap-northeast-2"
}

resource "aws_vpc" "simvex" {
  cidr_block           = "10.0.0.0/16" # 가성 네트워크 대역 설정
  enable_dns_hostnames = true          # DNS 호스트네임 활성화
  enable_dns_support   = true          # DNS 지원 활성화
  tags = {
    Name = "simvex-vpc"
  }
}

resource "aws_internet_gateway" "simvex" {
  vpc_id = aws_vpc.simvex.id
  tags = {
    Name = "simvex-igw"
  }
}

# 게이트웨이를 통해 외부와 직접 통신
resource "aws_subnet" "public_a" {
  vpc_id                  = aws_vpc.simvex.id
  cidr_block              = "10.0.1.0/24"     # 서브넷 대역 설정
  availability_zone       = "ap-northeast-2a" # 가용 영역 설정
  map_public_ip_on_launch = true              # 퍼블릭 IP 자동 할당 설정
  tags = {
    Name = "simvex-public-a"
  }
}

# 게이트웨이를 통해 외부와 직접 통신
resource "aws_subnet" "public_c" {
  vpc_id                  = aws_vpc.simvex.id
  cidr_block              = "10.0.2.0/24"     # 서브넷 대역 설정
  availability_zone       = "ap-northeast-2c" # 가용 영역 설정
  map_public_ip_on_launch = true              # 퍼블릭 IP 자동 할당 설정
  tags = {
    Name = "simvex-public-c"
  }
}


# 외부에서 직접 접근할 수 없는 내부 전용
resource "aws_subnet" "private_a" {
  vpc_id            = aws_vpc.simvex.id
  cidr_block        = "10.0.10.0/24"    # 서브넷 대역 설정
  availability_zone = "ap-northeast-2a" # 가용 영역 설정
  tags = {
    Name = "simvex-private-a"
  }
}

# 외부에서 직접 접근할 수 없는 내부 전용
resource "aws_subnet" "private_c" {
  vpc_id            = aws_vpc.simvex.id
  cidr_block        = "10.0.11.0/24"
  availability_zone = "ap-northeast-2c"
  tags = {
    Name = "simvex-private-c"
  }
}

# 라우트 테이블 생성
resource "aws_route_table" "public" {
  vpc_id = aws_vpc.simvex.id
  route {
    cidr_block = "0.0.0.0/0"                    # 모든 트래픽 허용
    gateway_id = aws_internet_gateway.simvex.id # 인터넷 게이트웨이 연결
  }
  tags = {
    Name = "simvex-public-rt"
  }
}

# 퍼블릭 서브넷과 라우트 테이블 연결
resource "aws_route_table_association" "public_a" {
  subnet_id      = aws_subnet.public_a.id
  route_table_id = aws_route_table.public.id
}
resource "aws_route_table_association" "public_c" {
  subnet_id      = aws_subnet.public_c.id
  route_table_id = aws_route_table.public.id
}



resource "aws_security_group" "ec2" {
  name   = "simvex-ec2-sg"
  vpc_id = aws_vpc.simvex.id
  ingress {
    description = "Allow HTTP (8080)"
    from_port   = 8080
    to_port     = 8080
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"] # 모든 곳에서 부트 포트 허용
  }
  ingress {
    description = "Allow SSH"
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"] # 모든 곳에서 SSH 허용
  }
  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"          # 모든 아웃바운드 트래픽 허용
    cidr_blocks = ["0.0.0.0/0"] # 모든 대상 허용
  }
}

# RDS용 보안 그룹
resource "aws_security_group" "rds" {
  name   = "simvex-rds-sg"
  vpc_id = aws_vpc.simvex.id
  ingress {
    description = "Allow MySQL"
    from_port   = 3306
    to_port     = 3306
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"] # 모든 곳에서 MySQL 허용
  }
}

# EC2 인스턴스가 ECR 이미지를 읽을 수 있도록 IAM 역할에 권한 부여
resource "aws_iam_role_policy_attachment" "ecr_readonly" {
  role       = aws_iam_role.ec2_role.name
  policy_arn = "arn:aws:iam::aws:policy/AmazonEC2ContainerRegistryReadOnly"
}

# RDS 서브넷 그룹 생성
resource "aws_db_subnet_group" "rds" {
  name       = "simvex-rds-subnet-group"
  subnet_ids = [aws_subnet.public_a.id, aws_subnet.public_c.id] # RDS가 사용할 서브넷
  tags = {
    Name = "simvex-rds-subnet-group"
  }
}

# RDS 인스턴스 생성
resource "aws_db_instance" "mysql" {
  identifier             = "simvex-rds"    # RDS 인스턴스 식별자
  engine                 = "mysql"         # 데이터베이스 엔진
  engine_version         = "8.0"           # MySQL 버전
  instance_class         = "db.t3.micro"   # 인스턴스 클래스
  allocated_storage      = 20              # 스토리지 크기
  db_name                = "simvex"        # 데이터베이스 이름
  username               = "rocket_user"   # 관리자 아이디
  password               = var.db_password # 관리자 비밀번호 (변수로 사용 예정)
  db_subnet_group_name   = aws_db_subnet_group.rds.name
  vpc_security_group_ids = [aws_security_group.rds.id]
  publicly_accessible    = true # 퍼블릭 액세스 허용
  skip_final_snapshot    = true # 삭제 시 백업 스냅샷 생성 x
}


# S3 버킷 생성
resource "aws_s3_bucket" "media" {
  bucket = "simvex-media-storage"
}

# EC2 인스턴스용 IAM 역할 생성
resource "aws_iam_role" "ec2_role" {
  name = "simvex-ec2-role"
  assume_role_policy = jsonencode({ # IAM 역할 신뢰 정책
    Version = "2012-10-17"
    Statement = [{
      Effect = "Allow",               # 역할 맡기 허용
      Principal = {                   # 역할을 맡길 서비스 지정
        Service = "ec2.amazonaws.com" # EC2 서비스에 역할 위임
      },
      Action = "sts:AssumeRole" # EC2가 이 역할을 맡을 수 있도록 허용
    }]
  })
}

# IAM 역할에 S3 접근 권한 부여
resource "aws_iam_role_policy" "s3_policy" {
  name = "simvex-ec2-s3-policy"
  role = aws_iam_role.ec2_role.id
  policy = jsonencode({
    Version = "2012-10-17",
    Statement = [{
      Effect = "Allow",
      Action = ["s3:PutObject",
        "s3:GetObject",
        "s3:ListBucket"] # 읽기, 쓰기, 목록 권한 허용
      Resource = [
        aws_s3_bucket.media.arn,       # 버킷 자체에 대한 권한
        "${aws_s3_bucket.media.arn}/*" # 버킷 및 버킷 내 모든 객체에 권한 적용
      ]
    }]
  })
}

resource "aws_iam_instance_profile" "profile" {
  name = "simvex-ec2-instance-profile"
  role = aws_iam_role.ec2_role.name
}


resource "aws_instance" "backend" {
  ami                         = "ami-0e9bfdb247cc8de84"               # OS 이미지 ID
  instance_type               = "t3.micro"                            # 서버 사양
  subnet_id                   = aws_subnet.public_a.id                # 퍼블릭 서브넷에 배치
  vpc_security_group_ids      = [aws_security_group.ec2.id]           # 보안 그룹 연결
  iam_instance_profile        = aws_iam_instance_profile.profile.name # S3 접근 권한 부여된 IAM 역할 연결
  associate_public_ip_address = true                                  # 퍼블릭 IP 자동 할당
  key_name                    = aws_key_pair.simvex.key_name          # SSH 접속용 키 페어

  user_data = <<-EOF
              #!/bin/bash
              set -e  # 에러 발생 시 즉시 중단 (문제 파악 용이)

              echo ">>> 시스템 업데이트 및 필수 도구 설치"
              sudo apt-get update -y
              sudo apt-get install -y ca-certificates curl gnupg unzip

              echo ">>> Docker GPG 키 및 레포지토리 설정"
              sudo install -m 0755 -d /etc/apt/keyrings
              curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /etc/apt/keyrings/docker.gpg
              sudo chmod a+r /etc/apt/keyrings/docker.gpg

              echo \
                "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg] https://download.docker.com/linux/ubuntu \
                $(. /etc/os-release && echo $VERSION_CODENAME) stable" | \
                sudo tee /etc/apt/sources.list.d/docker.list > /dev/null

              sudo apt-get update
              sudo apt-get install -y docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin

              echo ">>> 사용자 권한 설정"
              sudo usermod -aG docker ubuntu
              # user_data 실행 시점(root)에서는 바로 docker 명령 가능

              echo ">>> Docker 서비스 활성화 및 시작"
              sudo systemctl enable docker
              sudo systemctl start docker

              echo ">>> SSM Agent 설치 및 서비스 보장"
              sudo snap install amazon-ssm-agent --classic
              sudo systemctl enable snap.amazon-ssm-agent.amazon-ssm-agent.service || true
              sudo systemctl start snap.amazon-ssm-agent.amazon-ssm-agent.service || true

              echo ">>> Setup completed."
              EOF
  tags = {
    Name = "simvex-ec2"
  }
}

# SSH 접속용 키 페어 생성
resource "aws_key_pair" "simvex" {
  key_name   = "simvex-key"
  public_key = file("${path.module}/simvex-key.pub") # 로컬에 저장된 공개 키 파일 경로
}


# Elastic IP 할당 및 EC2 인스턴스에 연결
resource "aws_eip" "app" {
  instance = aws_instance.backend.id

  tags = {
    Name = "simvex-app-eip"
  }
}

# ECR 레포 생성
resource "aws_ecr_repository" "app" {
  name         = "simvex-backend"
  force_delete = true # 레포 삭제 시 이미지도 함께 삭제

  image_scanning_configuration {
    scan_on_push = true # 이미지 푸시 시 자동 스캔
  }

  tags = {
    Name = "simvex-backend-repo"
  }
}

data "aws_caller_identity" "current" {

}

resource "aws_ecr_repository_policy" "app_policy" {
  repository = aws_ecr_repository.app.name

  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Sid    = "AllowPushPull"
        Effect = "Allow"
        Principal = {
          AWS = "arn:aws:iam::${data.aws_caller_identity.current.account_id}:root"
        }
        Action = [
          "ecr:GetDownloadUrlForLayer", # 이미지 레이어 다운로드 권한
          "ecr:BatchGetImage", # 이미지 가져오기 권한
          "ecr:BatchCheckLayerAvailability", # 레이어 가용성 확인 권한
          "ecr:PutImage", # 이미지 푸시 권한
          "ecr:InitiateLayerUpload", # 레이어 업로드 시작 권한
          "ecr:UploadLayerPart", # 레이어 파트 업로드 권한
          "ecr:CompleteLayerUpload" # 레이어 업로드 완료 권한
        ]
      }
    ]
  })
}

output "ecr_repository_url"{
  value = aws_ecr_repository.app.repository_url
}

output "rds_endpoint"{
  value = aws_db_instance.mysql.endpoint
}

output "public_ip"{
  value = aws_eip.app.public_ip
}

output "ssh_command"{
  value = "ssh -i simvex-key.pem ubuntu@${aws_instance.backend.public_ip}"
}

output "instance_id" {
  value = aws_instance.backend.id
  description = "EC2 인스턴스 ID"
}

output "s3_bucket_name" {
  value = aws_s3_bucket.media.bucket
  description = "S3 버킷 이름"
}