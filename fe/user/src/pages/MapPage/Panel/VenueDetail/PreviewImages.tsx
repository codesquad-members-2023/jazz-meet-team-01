import styled from '@emotion/styled';

const PREVIEW_IMAGE_COUNT = 5;

export const PreviewImages: React.FC = () => {
  const images = [
    {
      id: 1,
      thumbnailUrl:
        'https://search.pstatic.net/common/?src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20150831_275%2F1441004893369gYh5w_JPEG%2F11611824_0.jpg',
      name: 'Evans',
      address: '서울 마포구 와우산로 63 2층',
    },
    {
      id: 2,
      thumbnailUrl:
        'https://search.pstatic.net/common/?src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20230408_221%2F1680929454542Aovwz_JPEG%2FKakaoTalk_20230214_192728451.jpg',
      name: '부기우기',
      address: '서울 용산구 회나무로 21 2층',
    },
    {
      id: 3,
      thumbnailUrl:
        'https://search.pstatic.net/common/?src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20210402_140%2F1617367262454YU3Hd_JPEG%2FNsOFazqvYXyXDXceOEjH3cm6.jpg',
      name: '블루밍 재즈바',
      address: '서울 강남구 테헤란로19길 21 지하1층',
    },
    {
      id: 4,
      thumbnailUrl:
        'https://search.pstatic.net/common/?src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20201118_121%2F1605662603727ABSSF_JPEG%2FKakaoTalk_20200919_012029316.jpg',
      name: '플랫나인',
      address: '서울 서초구 강남대로65길 10 5층',
    },
    {
      id: 5,
      thumbnailUrl:
        'https://search.pstatic.net/common/?src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20150831_275%2F1441004893369gYh5w_JPEG%2F11611824_0.jpg',
      name: 'Evans',
      address: '서울 마포구 와우산로 63 2층',
    },
    {
      id: 6,
      thumbnailUrl:
        'https://search.pstatic.net/common/?src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20230408_221%2F1680929454542Aovwz_JPEG%2FKakaoTalk_20230214_192728451.jpg',
      name: '부기우기',
      address: '서울 용산구 회나무로 21 2층',
    },
    {
      id: 7,
      thumbnailUrl:
        'https://search.pstatic.net/common/?src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20210402_140%2F1617367262454YU3Hd_JPEG%2FNsOFazqvYXyXDXceOEjH3cm6.jpg',
      name: '블루밍 재즈바',
      address: '서울 강남구 테헤란로19길 21 지하1층',
    },
    {
      id: 8,
      thumbnailUrl:
        'https://search.pstatic.net/common/?src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20201118_121%2F1605662603727ABSSF_JPEG%2FKakaoTalk_20200919_012029316.jpg',
      name: '플랫나인',
      address: '서울 서초구 강남대로65길 10 5층',
    },
    {
      id: 9,
      thumbnailUrl:
        'https://search.pstatic.net/common/?src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20210402_140%2F1617367262454YU3Hd_JPEG%2FNsOFazqvYXyXDXceOEjH3cm6.jpg',
      name: '블루밍 재즈바',
      address: '서울 강남구 테헤란로19길 21 지하1층',
    },
    {
      id: 10,
      thumbnailUrl:
        'https://search.pstatic.net/common/?src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20201118_121%2F1605662603727ABSSF_JPEG%2FKakaoTalk_20200919_012029316.jpg',
      name: '플랫나인',
      address: '서울 서초구 강남대로65길 10 5층',
    },
  ];

  return (
    <StyledImages>
      {images.slice(0, PREVIEW_IMAGE_COUNT).map((image) => (
        <StyledImageWrapper key={image.id} imagesLength={images.length}>
          <StyledImage src={image.thumbnailUrl} />
        </StyledImageWrapper>
      ))}
    </StyledImages>
  );
};

const StyledImages = styled.div`
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  grid-template-rows: repeat(2, 1fr);
  gap: 4px;
`;

const StyledImageWrapper = styled.div<{ imagesLength: number }>`
  position: relative;
  width: 100%;
  padding-top: 100%;

  &:first-of-type {
    grid-column: 1 / 3;
    grid-row: 1 / 3;
  }

  &:last-of-type {
    &::after {
      position: absolute;
      inset: 0;
      width: 100%;
      height: 100%;
      color: #fff;
      background: rgba(0, 0, 0, 0.3);
      content: '+${({ imagesLength }) => imagesLength - PREVIEW_IMAGE_COUNT}';
      display: flex;
      justify-content: center;
      align-items: center;
    }
  }
`;

const StyledImage = styled.img`
  position: absolute;
  inset: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
`;
